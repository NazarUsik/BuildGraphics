package paint;

import javafx.scene.control.TextField;

/**
 * control elements on scene
 */

public class NumberTextField extends TextField {

    public NumberTextField(){
        super.setMaxSize(45,35);
        super.setMinSize(45,35);
    }

    @Override
    public void replaceText(int start, int end, String text) {
        String oldValue = getText();
        if (validator(text)) {
            super.replaceText(start, end, text);
            String newText = super.getText();
            if (!validator(newText))
                super.setText(oldValue);
        }
    }

    private boolean validator(String text) {
        String numberRegEx = "[-9.0-9.0]*";
//        String numberSing = "[.]*";
//        String negativeNumber = "[-]*";
        return ("".equals(text) || text.matches(numberRegEx));
    }
}
