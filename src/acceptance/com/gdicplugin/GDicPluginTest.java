package acceptance.com.gdicplugin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tonyx.GoogleDicContentFilter;
import org.tonyx.GoogleDicProvider;
import org.tonyxzt.language.core.GenericDictionary;
import org.tonyxzt.language.core.Translator;
import org.tonyxzt.language.io.InMemoryOutStream;
import org.tonyxzt.language.io.InputStream;
import org.tonyxzt.language.util.FakeBrowserActivator;

import java.util.HashMap;
import java.util.Map;

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
    InMemoryOutStream ios;
    FakeBrowserActivator browserActivator;

    @Before
    public void SetUp() {
        browserActivator = new FakeBrowserActivator();
        mapDictionaries = new HashMap<String,GenericDictionary>(){
               {
                    put("gDic",new GenericDictionary("gDic",new GoogleDicProvider(),new GoogleDicContentFilter()));
               }
        };
        ios = new InMemoryOutStream();
        //translator = new Translator(mapDictionaries);
        translator = new Translator(mapDictionaries,browserActivator,ios);
    }

    @Test
    public void canTranslateHello() throws Exception {
        translator.setCommand(new String[]{"--dic=gDic", "--oriLang=it", "--targetLang=en", "ciao"});
        translator.setOutStream(ios);
        translator.doAction();
        Assert.assertTrue(ios.getContent().contains("bye"));
    }


    @Test
    public void canGetTheUrlService() {
        translator.setCommand(new String[] {"--dic=gDic", "--info"});
        translator.doAction();
        Assert.assertEquals("http://www.google.com/dictionary", browserActivator.getOutUrl());
    }

    @Test
    public void canReadFromInputFile() throws Exception {
        translator.setCommand(new String[] {"--dic=gDic","--oriLang=en","--targetLang=it","--inFile=infile"});
        InputStream inputStream = new InputStream(){ boolean start = true; public String next() {if (start) { start=false; return "hi";} else return null;}};
        translator.setInputStream(inputStream);
        translator.setOutStream(ios);
        translator.doAction();

        Assert.assertTrue(ios.getContent().contains("ciao"));
        Assert.assertTrue(ios.getContent().contains("salve"));
    }

    @Test
    public void testCyrillic() throws Exception {
        translator.setCommand(new String[]{"--dic=gDic","--oriLang=en","--targetLang=ru","hi"});
        translator.setOutStream(ios);
        translator.doAction();

        Assert.assertTrue(ios.getContent().contains("Гавайи"));

    }
}

