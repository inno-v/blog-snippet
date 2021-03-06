import java.io.*
import org.jdom.*
import org.jdom.input.*
import org.jdom.xpath.*
import org.jdom.output.*
import org.xml.sax.*

def xhtml = """<html xmlns="http://www.w3.org/1999/xhtml"
  xmlns:html="http://www.w3.org/1999/xhtml"
  xmlns:myns="http://www.w3.org/1999/xhtml"
  >
<head><title>Namespace Prefix Test</title></head>
<body>
<span>prefix:default</span>
<html:span html:id="html">prefix:html</html:span>
<myns:span myns:id="myns">prefix:myns</myns:span>
</body>
</html>"""

def xpaths = [
  "" : "//span",
  "html" : "//html:span",
  "myns" : "//myns:span"
  ]

static class ParserHack extends org.ccil.cowan.tagsoup.Parser {
  @Override
  public void setFeature (String name, boolean value) {
    System.out.println( "ParserHack: setting feature: " + name + " to " + value )
    if ( name.equals( "http://xml.org/sax/features/namespaces" ) ) {
      super.setFeature( name, false );
    }
    else {
      super.setFeature( name, value );
    }
  }
}

def is = new InputSource( new ByteArrayInputStream( xhtml.bytes ) )
def outputter = new XMLOutputter()
def builder = new SAXBuilder( ParserHack.class.canonicalName )
def doc = builder.build( is )
println "input:\n" + xhtml
println "output:\n" + outputter.outputString( doc )
println "result:\n"
xpaths.each { prefix, path ->
  println "namespace prefix: \"${prefix}\"; xpath: \"${path}\""
  def xpath = XPath.newInstance( path )
  xpath.addNamespace( prefix, "http://www.w3.org/1999/xhtml" )
  def result = xpath.selectNodes( doc )
  result.each { println "  " + outputter.outputString( it ) }
}
