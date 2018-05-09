package top.wuhaojie.se.entity;

import org.apache.http.util.TextUtils;
import org.jdesktop.swingx.ux.CellProvider;
import org.jdesktop.swingx.ux.Selector;

/**
 * Created by dim on 2015/7/15.
 */
public class FieldEntity implements Selector, CellProvider {

    private String key;
    private String type; //类型
    private String fieldName; // 生成的名字
    private String value; // 值
    private boolean generate = true;


    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getGenerateFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return;
        }
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public String getRealType() {
        return type;
    }

    public String getBriefType() {
        return type;
    }

    public String getFullNameType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void setSelect(boolean select) {
        setGenerate(select);
    }


    @Override
    public String getCellTitle(int index) {
        String result = "";
        switch (index) {
            case 0:
                result = getKey();
                break;
            case 1:
                result = getValue();
                break;
            case 2:
                result = getBriefType();
                break;
            case 3:
                result = getFieldName();
                break;
        }
        return result;
    }


    @Override
    public void setValueAt(int column, String text) {
        switch (column) {
            case 2:
//                checkAndSetType(text);
                break;
            case 3:
//                if(CheckUtil.getInstant().containsDeclareFieldName(text)){
//                    return;
//                }
//                CheckUtil.getInstant().removeDeclareFieldName(getFieldName());
                setFieldName(text);
                break;
        }
    }
}
