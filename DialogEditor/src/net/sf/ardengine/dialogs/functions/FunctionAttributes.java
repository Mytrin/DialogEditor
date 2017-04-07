package net.sf.ardengine.dialogs.functions;

import java.util.HashMap;
import net.sf.ardengine.dialogs.Execute;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Contains basic information about function called owner
 */
public class FunctionAttributes {

    private final HashMap<String, String> attributesMap = new HashMap<>();

    private String functionName;
    private final String functionAttrName;

    /**
     * @param sourceElement element, which might call function
     * @param functionAttrName element attribute containing function name
     */
    public FunctionAttributes(Element sourceElement, String functionAttrName) {
        functionName = sourceElement.getAttributeValue(functionAttrName);
        this.functionAttrName = functionAttrName;

        if (functionName != null) {
            attributesMap.put(functionAttrName, functionName);

            for (Attribute attr : sourceElement.getAttributes()) {
                attributesMap.put(attr.getName(), attr.getValue());
            }
        }
    }

    /**
     * @param functionAttrName element attribute containing function name
     */
    public FunctionAttributes(String functionAttrName) {
        functionName = null;
        this.functionAttrName = functionAttrName;
    }

    /**
     * Changes function used by element.
     *
     * @param functionName name of new function
     */
    public void setFunction(String functionName) {
        this.functionName = functionName;
        attributesMap.put(functionAttrName, functionName);
    }

    /**
     * @return name of used function or null
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * @param attrName Name of desired attribute
     * @return Value of attribute, if exists, otherwise null
     */
    public String getAttributeValue(String attrName) {
        return attributesMap.get(attrName);
    }

    /**
     * @param attrName Name of desired attribute
     * @param attrValue new attribute value
     */
    public void setAttribute(String attrName, String attrValue) {
        attributesMap.put(attrName, attrValue);
    }

    /**
     * Attach all loaded attributes to this element
     *
     * @param element New function element
     */
    public void save(Element element) {
        for (String key : attributesMap.keySet()) {
            element.setAttribute(key, attributesMap.get(key));
        }
    }

    /**
     *
     * @return String that contain all attributes and its values except
     * functionName
     */
    public String getAllAttributesWithoutName() {
        String toReturn = "";
        for (String key : attributesMap.keySet()) {
            if (!key.equals(Execute.ATTR_FUNCTION_NAME)) {
                toReturn += key + ":" + attributesMap.get(key) + ",";
            }
        }
        return toReturn.substring(0, toReturn.length() - 1);
    }

}
