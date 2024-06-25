package za.co.dfmsoftware.utility.model.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * PROBE
 */
public class Probe extends RealmObject {
    @PrimaryKey
    @SerializedName("ProbeID")
    private int probeId;

    @SerializedName("BlockID")
    private int blockId;

    @SerializedName("Customer")
    private String customer;

    @SerializedName("CustomerID")
    private int customerId;

    @SerializedName("Farm") //todo use this as the name for the customer
    private String farm;

    @SerializedName("FarmID")
    private int farmId;

    @SerializedName("ProbeStatus")
    private int probeStatus;

    @SerializedName("Probe") //this is the block name
    private String probe;

    @SerializedName("ProbeLength")
    private int probeLength;

    public Probe() {} //default Constructor

    //GETTERS
    public int getProbeId() {
        return probeId;
    }

    public int getBlockId() {
        return blockId;
    }

    public String getCustomer() {
        return customer;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFarm() {
        return farm;
    }

    public int getFarmId() {
        return farmId;
    }

    public int getProbeStatus() {
        return probeStatus;
    }

    public String getProbe() { return probe; } //this is the block name

    public int getProbeLength() {
        return probeLength;
    }

    //SETTERS
    public void setProbeId(int probeId) {
        this.probeId = probeId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public void setFarmId(int farmId) {
        this.farmId = farmId;
    }

    public void setProbeStatus(int probeStatus) {
        this.probeStatus = probeStatus;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public void setProbeLength(int probeLength) {
        this.probeLength = probeLength;
    }
}
