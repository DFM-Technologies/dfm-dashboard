package za.co.dfmsoftware.utility.model.enums;

public enum ProbeStatus {
    NONE(0),
    VERY_WET(1),
    WET(2),
    GOOD(3),
    ALMOST_DRY(4),
    DRY(5),
    VERY_DRY(6),

    ALL_PROBES(9999);

    int status;

    ProbeStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public static ProbeStatus getStatus(int status) {
        switch (status) {
            case 1:
                return VERY_WET;
            case 2:
                return WET;
            case 3:
                return GOOD;
            case 4:
                return ALMOST_DRY;
            case 5:
                return DRY;
            case 6:
                return VERY_DRY;
            case 9999:
                return ALL_PROBES;
            default:
                return NONE;
        }
    }
}
