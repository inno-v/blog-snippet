import java.io.FileWriter
import org.jdom.*
import org.jdom.input.*
import org.jdom.xpath.*
import org.jdom.output.*
import org.xml.sax.*

def builder = new SAXBuilder( "org.ccil.cowan.tagsoup.Parser" )
def xpath = XPath.newInstance( "//html:span[contains(@class,'titletext')]/text()" )
def is = new InputSource( "http://news.google.com.tw/" )
is.setEncoding( "UTF-8" )
def doc = builder.build( is )
new XMLOutputter().output( doc, new FileWriter( "output.html" ) )
def result = xpath.selectNodes( doc )
result.each { println it.value }
