package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import java.util.Comparator;

import teckvillage.developer.khaled_pc.teckvillagetrue.model.ContactInfo;

/**
 * Created by khaled-pc on 2/13/2019.
 */

public class LetterComparator implements Comparator<ContactInfo> {

@Override
public int compare(ContactInfo l, ContactInfo r) {
        if (l == null || r == null) {
        return 0;
        }

        String lhsSortLetters = l.contacName.substring(0, 1).toUpperCase();
        String rhsSortLetters = r.contacName.substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
        return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
        }
        }
