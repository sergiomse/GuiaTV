package cartelera.turnodetarde.example.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LinearLayout iconsLayout;

    private List<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconsLayout = (LinearLayout) findViewById(R.id.iconsLayout);

        loadChannels();

        populateIconsLayout();
    }

    private void loadChannels() {
        for(int i=61; i<149; i++) {
            Channel channel = new Channel();
            channel.setName("channel-" + String.valueOf(i));
            channels.add(channel);
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.tvguia.es/program/ajax/6/1/7/7/9/0/FFFFFF/FFFFFF/313131/111111";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    response = response.replaceAll("&nbsp;", " ");
                    response = "<div>" + response + "</div>";

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true);
                    DocumentBuilder builder;
                    Document doc = null;

                    InputSource is = new InputSource(new StringReader(response));

                    try {
                        builder = factory.newDocumentBuilder();
                        doc = builder.parse(is);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for(Channel channel : channels) {
                        XPath xpath = XPathFactory.newInstance().newXPath();
                        String expression = "/div/div[@id='" + channel.getName() + "']/div";
                        try {
                            NodeList nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);

                            for (int i = 1; i < nodes.getLength(); i++) {
                                Node node = nodes.item(i);
                                loadPrograms(node, channel);
                            }
                        } catch (XPathExpressionException e) {
                            e.printStackTrace();
                        }
                    }

                }

                private void loadPrograms(Node node, Channel channel) {
                    StringWriter sw = new StringWriter();
                    try {
                        Transformer t = TransformerFactory.newInstance().newTransformer();
                        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                        t.setOutputProperty(OutputKeys.INDENT, "yes");
                        t.transform(new DOMSource(node), new StreamResult(sw));
                    } catch (TransformerException te) {
                        System.out.println("nodeToString Transformer Exception");
                    }
                    Log.d(TAG, sw.toString());
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    String expression = "./div/a";

                    try {
                        Node textNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
                        Log.d(TAG, textNode.getTextContent());
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                }

            },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error conexion", Toast.LENGTH_LONG).show();
                }

            });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void populateIconsLayout() {
        for(int i=0; i<20; i++) {
            TextView text = new TextView(this);
            text.setText(String.valueOf(i));
            text.setTextSize(24);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.left_panel_width));
            text.setLayoutParams(params);
            text.setGravity(Gravity.CENTER);

            iconsLayout.addView(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
