package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * Created by Fabio on 14.03.2015.
 */
public enum Additive {
    /** mit Farbstoff */
    DYE(1),
    /** mit Konservierungsstoff */
    PRESERVATIVE(2),
    /** mit Antioxidationsmittel */
    ANTIOXIDANT(3),
    /** mit Geschmacksverstärker */
    FLAVOR_ENHANCERS(4),
    /** geschwefelt */
    SULPHURED(5),
    /** geschwärzt (Oliven) */
    BLACKENED(6),
    /** mit Phosphat */
    PHOSPHATE(8),
    /** mit Süßungsmitteln */
    SWEETENERS(9),
    /** enthält eine Phenylalaninquelle */
    PHENYLALANINQUELLE(10),
    /** mit einer Zuckerart und Süßungsmitteln */
    SUGAR_AND_SWEETENERS(11),
    /** mit Alkohol */
    ALCOHOL(99),
    // Hier kommen keine Zusatzstoffe, sondern Bestandteile der Mahlzeit!
    /** mit Schweinefleisch */
    PIG("S"),
    /** mit Rindfleisch */
    BEEF("R"),
    // Organisationen
    /** Zertifizierte Nachhaltige Fischerei - MSC*/
    MSC("MSC"),
    /** Geprüfte Qualität - Bayern*/
    GQB("GQB");

    private final int signInt;
    private final String signStr;

    private Additive(final int signInt) {
        this.signInt = signInt;
        this.signStr = null;
    }

    Additive(final String sign) {
        this.signInt = -1;
        this.signStr = sign;
    }

    public int getSignInt() {
        return signInt;
    }

    public String getSignStr() {
        return signStr;
    }

    @Override
    public String toString() {
        return signStr == null ? Integer.toString(signInt) : signStr;
    }

    public static Additive of(String label) {
        for (Additive additive : values()) {
            if(additive.toString().equalsIgnoreCase(label)) {
                return additive;
            }
        }
        return null;
    }
}
