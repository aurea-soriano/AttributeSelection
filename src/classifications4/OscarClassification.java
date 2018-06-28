/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifications4;

/**
 *
 * @author aurea
 */
public class OscarClassification implements S4Classification {

    String description = "4 - 2.64 <=value  \n"
            + "3 - 2.22 <= value < 2.64\n"
            + "2 - 1.8 <= value < 2.22 \n"
            + "1 -        value < 1.8 \n";

    @Override
    public float calculateLabel(float value) {
        if (2.64 <= value) {
            return 4.f;
        } else {
            if (2.22 <= value && value < 2.64) {
                return 3.f;
            } else {
                if (1.8 <= value && value < 2.22) {
                    return 2.f;
                } else {
                    return 1.f;
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return description;
    }
}
