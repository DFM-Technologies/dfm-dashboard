package za.co.dfmsoftware.utility.realm;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import za.co.dfmsoftware.utility.Logger;
import za.co.dfmsoftware.utility.model.enums.ProbeStatus;
import za.co.dfmsoftware.utility.model.model.Probe;
import za.co.dfmsoftware.utility.model.model.User;

/**
 * DFM Realm
 * */
public class DfmRealm {

    private static DfmRealm INSTANCE;

    public static DfmRealm getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DfmRealm();
        }

        return INSTANCE;
    }

    private DfmRealm() {} //default constructor

    public void initRealm(@NonNull Context context) { Realm.init(context); }

    /** USER CONTEXT**/
    public void addOrUpdateUser(@NonNull User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            Logger.e("Error","User has no username. Cannot add or update.");
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(user);
            realm.commitTransaction();
        } catch (Exception e) {
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            Logger.e("Error","Error adding/updating user in Realm");
        } finally {
            realm.close();
        }
    }

    public User getCurrentUser() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(User.class).findFirst();
    }

    /** PROBE CONTEXT **/
    public List<Probe> getAllProbes() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Probe> probeList = realm.where(Probe.class);
        return realm.copyFromRealm(probeList.findAll());
    }

    public int getProbesCount(){
        Realm realm = Realm.getDefaultInstance();
        return (int) realm.where(Probe.class).count();
    }

    public Probe getProbe(int probeId){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Probe.class).equalTo("probeId", probeId).findFirst();
    }

    public List<Probe> getAllProbesForStatus(@NonNull ProbeStatus probeStatus){
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Probe> probeList = realm.where(Probe.class).equalTo("probeStatus", probeStatus.getStatus());
        return realm.copyFromRealm(probeList.findAll());
    }

    public HashMap<ProbeStatus, Integer> getAllProbesWithStatus() {
        Realm realm = Realm.getDefaultInstance();
        HashMap<ProbeStatus, Integer> probeStatus = new HashMap<>(7);

        for(int i = 1; i < 7; i++){
            int count = (int) realm.where(Probe.class).equalTo("probeStatus", i).count();

            if(count == 0) {
                continue;
            }

            probeStatus.put(ProbeStatus.getStatus(i), count);
        }

        return probeStatus;
    }

    public void addOrUpdateProbes(@NonNull List<Probe> probeList){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(probeList);
        realm.commitTransaction();
    }

    public void clearAllData(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
}
