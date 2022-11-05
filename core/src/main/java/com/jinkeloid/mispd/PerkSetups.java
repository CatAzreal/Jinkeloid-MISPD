package com.jinkeloid.mispd;

import com.jinkeloid.mispd.actors.hero.HeroClass;
import com.jinkeloid.mispd.actors.hero.HeroSubClass;
import com.jinkeloid.mispd.actors.hero.Perk;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class PerkSetups {
    //Yes I'm using logic from GamesInProgress to save perk setups, stop questioning me
    public static final int MAX_SLOTS = 6;

    private static HashMap<Integer, PerkSetups.Info> slotStates = new HashMap<>();
    public static int curSlot;

    private static final String SETUP_FILE  = "setup%d.dat";
    private static final String PERKS		= "perks";
    private static final String SLOT        = "slot";
    private static final String POSCOUNT	= "poscount";
    private static final String NEGCOUNT	= "negcount";
    private static final String SCORE		= "score";
    private static final String NAME		= "name";

    public static boolean setupExists( int slot ){
        return FileUtils.fileExists(Messages.format(SETUP_FILE, slot));
    }

    public static String setupFile( int slot ){
        return Messages.format(SETUP_FILE, slot);
    }

    public static ArrayList<PerkSetups.Info> checkAll(){
        ArrayList<PerkSetups.Info> result = new ArrayList<>();
        for (int i = 0; i <= MAX_SLOTS - 1 ; i++){
            PerkSetups.Info curr = check(i);
            result.add(curr);
        }
        Collections.sort(result, scoreComparator);
        return result;
    }

    public static PerkSetups.Info check(int slot ) {
        PerkSetups.Info info;
        if (slotStates.containsKey( slot )) {
            info = slotStates.get( slot );
            if (info == null){
                info = new PerkSetups.Info();
                info.slot = slot;
                info.exist = false;
                info.isNew = false;
                slotStates.put( slot, info );
                return info;
            }
            //
            if (!info.isNew)
                return slotStates.get( slot );
        } else if (!setupExists( slot )) {
            GLog.i("setup doesn't exist in slot " + slot);
            //If a setup doesn't exist, we have to autofill it with an empty setup.
            info = new PerkSetups.Info();
            info.slot = slot;
            info.exist = false;
            info.isNew = false;
//            info.Perks = new ArrayList<Perk>(Arrays.asList(
//                    Perk.CATS_EYES,
//                    Perk.INCONSPICUOUS,
//                    Perk.ABSTINENCE,
//                    Perk.CLUMSY,
//                    Perk.LACK_OF_SENSE,
//                    Perk.DISORGANIZED));
            slotStates.put( slot, info );
            return info;
        }
        info = new PerkSetups.Info();
        try {
            GLog.i("trying to fetch setups from slot " + slot);
            Bundle bundle = FileUtils.bundleFromFile(setupFile(slot));
            info.slot = slot;
            int[] perkListInt = bundle.getIntArray( PERKS );
            info.Perks = new ArrayList<>();
            for (int i : perkListInt) {
                info.Perks.add(Perk.getPerkByID(i));
            }
            info.posCount = bundle.getInt( POSCOUNT );
            info.negCount = bundle.getInt( NEGCOUNT );
            info.score = bundle.getInt( SCORE );
            info.setupName = bundle.getString( NAME );
            info.exist = true;
            info.isNew = false;
        } catch (IOException e) {
            info.setupName = "LOAD ERROR";
            info.exist = false;
        } catch (Exception e){
            MusicImplantSPD.reportException( e );
            info.setupName = "LOAD ERROR";
            info.exist = false;
        }

        slotStates.put( slot, info );
        return info;
    }

    public static void saveInfo( ArrayList<Perk> perkList, int slot, String setupName) {
        Bundle bundle = new Bundle();
        ArrayList<Integer> perkListInt = new ArrayList<>();
        for (Perk perk : perkList){
            perkListInt.add(perk.id());
        }
        int score, posCount, negCount;
        score = posCount = negCount = 0;
        for (Perk perk : perkList){
            if (perk.isPositive()) { negCount++; } else { posCount++; }
            score += perk.isPositive() ? perk.pointCosts() : - perk.pointCosts();
        }
        bundle.put( PERKS, perkListInt );
        bundle.put( POSCOUNT, posCount);
        bundle.put( NEGCOUNT, negCount);
        bundle.put( SCORE, score);
        bundle.put( NAME, setupName);
        try {
            slotStates.put( slot, new Info(slot, posCount, negCount, score, setupName, perkList));
            FileUtils.bundleToFile(setupFile(slot), bundle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Info {
        public int slot;

        public int posCount;
        public int negCount;
        public int score;

        public String setupName;
        public boolean exist = true;
        public ArrayList<Perk> Perks;
        public boolean isNew = true;

        public Info(){}
        public Info(int slot, int posCount, int negCount, int score, String setupName, ArrayList<Perk> perks) {
            this.slot = slot;
            this.posCount = posCount;
            this.negCount = negCount;
            this.score = score;
            this.setupName = setupName;
            this.Perks = perks;
        }
    }

    public static final Comparator<PerkSetups.Info> scoreComparator = new Comparator<PerkSetups.Info>() {
        @Override
        public int compare(PerkSetups.Info lhs, PerkSetups.Info rhs ) {
            return (int)Math.signum( (rhs == null ? -1000 : rhs.score) - (lhs == null ? -1000 : lhs.score) );
        }
    };
}
