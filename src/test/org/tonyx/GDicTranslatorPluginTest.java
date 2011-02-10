package test.org.tonyx;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tonyx.GoogleDicContentFilter;
import org.tonyxzt.language.core.ContentProvider;
import org.tonyxzt.language.core.GenericDictionary;
import org.tonyxzt.language.core.Translator;
import org.tonyxzt.language.io.InMemoryOutStream;
import org.tonyxzt.language.io.InputStream;
import org.tonyxzt.language.io.OutStream;
import org.tonyxzt.language.util.BrowserActivator;
import org.tonyxzt.language.util.FakeBrowserActivator;
import test.com.tonyxzt.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tonino
 * Date: 27/01/11
 * Time: 17.41
 * To change this template use File | Settings | File Templates.
 */
public class GDicTranslatorPluginTest {
    Map<String,GenericDictionary> mapMockedDictionaries;
    private Translator translator;
    OutStream outStream;
    BrowserActivator activator;
    ContentProvider gDicContentProvider;


    @Before
    public void SetUp() {
        outStream = mock(OutStream.class);
        activator = mock(BrowserActivator.class);
        mapMockedDictionaries = new HashMap<String,GenericDictionary>();
        gDicContentProvider = mock(ContentProvider.class);
        mapMockedDictionaries.put("gDic",new GenericDictionary("gDic",gDicContentProvider,new GDicContentFilter()));

        translator = new Translator(mapMockedDictionaries,activator,outStream);
    }



    @Test
    public void canReadFromInputFileMultipleLines() throws Exception {

        InputStream inputStream = mock(InputStream.class) ;
        when(inputStream.next()).thenReturn("hi").thenReturn("hi").thenReturn("hi").thenReturn(null);
        when(gDicContentProvider.retrieve(anyString(),anyString(),anyString())).thenReturn(test.com.tonyxzt.StubbedGHtmlContent.content);

        translator.setCommand(new String[] {"--dic=gDic", "--oriLang=it","--targetLang=en","--inFile=infile"});
        translator.setInputStream(inputStream);
        translator.doAction();

        verify(outStream,times(3)).output("hi = salut!, bonjour!, hé!, ");

    }




}
