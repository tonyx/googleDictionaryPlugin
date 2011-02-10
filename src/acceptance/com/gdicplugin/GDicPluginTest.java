package acceptance.com.gdicplugin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.tonyx.GoogleDicContentFilter;
import org.tonyx.GoogleDicProvider;
import org.tonyxzt.language.core.GenericDictionary;
import org.tonyxzt.language.core.Translator;
import org.tonyxzt.language.io.InputStream;
import org.tonyxzt.language.io.OutStream;
import org.tonyxzt.language.util.BrowserActivator;
import org.tonyxzt.language.util.FakeBrowserActivator;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 27/01/11
 * Time: 1.48
 * To change this template use File | Settings | File Templates.
 */
public class GDicPluginTest {
    Map<String,GenericDictionary> mapDictionaries;
    private Translator translator;
    OutStream ios;
    BrowserActivator browserActivator;

    @Before
    public void SetUp() {
        browserActivator = mock(FakeBrowserActivator.class);
        ios = mock(OutStream.class);

        mapDictionaries = new HashMap<String,GenericDictionary>(){
               {
                    put("gDic",new GenericDictionary("gDic",new GoogleDicProvider(),new GoogleDicContentFilter()));
               }
        };
        translator = new Translator(mapDictionaries,browserActivator,ios);
    }

    @Test
    public void canTranslateHello() throws Exception {
        ArgumentMatcher<String> containsBye = new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object object) {
                return ((String)object).contains("bye");
            }
        };

        translator.setCommand(new String[]{"--dic=gDic", "--oriLang=it", "--targetLang=en", "ciao"});
        translator.doAction();

        verify(ios).output(argThat(containsBye));
    }


    @Test
    public void canGetTheUrlService() {
        translator.setCommand(new String[] {"--dic=gDic", "--info"});
        translator.doAction();

        verify(browserActivator).activateBrowser("http://www.google.com/dictionary");
    }

    @Test
    public void canReadFromInputFile() throws Exception {
        ArgumentMatcher<String> containsBye = new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object object) {
                return ((String)object).contains("ciao")&&((String)object).contains("salve");
            }
        };
        translator.setCommand(new String[] {"--dic=gDic","--oriLang=en","--targetLang=it","--inFile=infile"});

        InputStream inputStream = mock(InputStream.class);
        when(inputStream.next()).thenReturn("hi").thenReturn(null);

        translator.setInputStream(inputStream);
        translator.doAction();
        verify(ios).output(argThat(containsBye));
    }

    @Test
    public void testCyrillic() throws Exception {
        ArgumentMatcher<String> containsDavai = new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object object) {
                return ((String)object).contains("Гавайи");
            }
        };
        translator.setCommand(new String[]{"--dic=gDic","--oriLang=en","--targetLang=ru","hi"});
        translator.doAction();

        verify(ios).output(argThat(containsDavai));
    }
}

