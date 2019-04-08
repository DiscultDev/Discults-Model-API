package discult.modelapi.client.loaders.collada;


import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlParser {

    private static final Pattern DATA = Pattern.compile(">(.+?)<");
    private static final Pattern START_TAG = Pattern.compile("<(.+?)>");
    private static final Pattern ATTR_NAME = Pattern.compile("(.+?)=");
    private static final Pattern ATTR_VAL = Pattern.compile("\"(.+?)\"");
    private static final Pattern CLOSED = Pattern.compile("(</|/>)");


    public static XmlNode loadXmlFile(Identifier resourceLocation)
    {

        InputStream stream;
        BufferedReader reader;
        try {
            stream = MinecraftClient.getInstance().getResourceManager().getResource(resourceLocation).getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can't find the XML file: " + resourceLocation.getPath() + ". For Mod: " + resourceLocation.getNamespace());
            System.exit(0);
            return null;
        }
        try {
            reader.readLine();
            XmlNode node = loadNode(reader);
            reader.close();
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error with XML file format for: " + resourceLocation.getPath() + ". For Mod: " + resourceLocation.getNamespace());
            System.exit(0);
            return null;
        }
    }

    private static XmlNode loadNode(BufferedReader reader) throws Exception {
        String line = reader.readLine().trim();
        if (line.startsWith("</")) {
            return null;
        }
        String[] startTagParts = getStartTag(line).split(" ");
        XmlNode node = new XmlNode(startTagParts[0].replace("/", ""));
        addAttributes(startTagParts, node);
        addData(line, node);
        if (CLOSED.matcher(line).find()) {
            return node;
        }
        XmlNode child = null;
        while ((child = loadNode(reader)) != null) {
            node.addChild(child);
        }
        return node;
    }

    private static void addData(String line, XmlNode node) {
        Matcher matcher = DATA.matcher(line);
        if (matcher.find()) {
            node.setData(matcher.group(1));
        }
    }

    private static void addAttributes(String[] titleParts, XmlNode node) {
        for (int i = 1; i < titleParts.length; i++) {
            if (titleParts[i].contains("=")) {
                addAttribute(titleParts[i], node);
            }
        }
    }

    private static void addAttribute(String attributeLine, XmlNode node) {
        Matcher nameMatch = ATTR_NAME.matcher(attributeLine);
        nameMatch.find();
        Matcher valMatch = ATTR_VAL.matcher(attributeLine);
        valMatch.find();
        node.addAttribute(nameMatch.group(1), valMatch.group(1));
    }

    private static String getStartTag(String line) {
        Matcher match = START_TAG.matcher(line);
        match.find();
        return match.group(1);
    }

}

/**
 * XMLNode Class
 * This Represents a single Node within the XML file.
 */
class XmlNode
{
    private String name, data;
    private Map<String, String> attributes;
    private Map<String, List<XmlNode>> childNodes;

    protected XmlNode(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAttribute(String attr)
    {
        if(attributes != null) return attributes.get(attr);
        else return null;
    }

    public XmlNode getChild(String childName)
    {
        if(childNodes != null)
        {
            List<XmlNode> nodes = childNodes.get(childName);

            if( nodes != null && !nodes.isEmpty()) return nodes.get(0);
        }

        return null;
    }

    public XmlNode getChildWithAttribute(String childName, String attr, String value) {
        List<XmlNode> children = getChildren(childName);
        if (children == null || children.isEmpty()) return null;

        for (XmlNode child : children)
        {
            String val = child.getAttribute(attr);
            if (value.equals(val)) return child;
        }
        return null;
    }

    public List<XmlNode> getChildren(String name) {
        if (childNodes != null) {
            List<XmlNode> children = childNodes.get(name);

            if (children != null) return children;
        }
        return new ArrayList<>();
    }

    protected void addAttribute(String attr, String value) {
        if (attributes == null) attributes = new HashMap<>();

        attributes.put(attr, value);
    }

    protected void addChild(XmlNode child) {
        if (childNodes == null) childNodes = new HashMap<>();

        List<XmlNode> list = childNodes.get(child.name);
        if (list == null) {
            list = new ArrayList<>();
            childNodes.put(child.name, list);
        }
        list.add(child);
    }

}

