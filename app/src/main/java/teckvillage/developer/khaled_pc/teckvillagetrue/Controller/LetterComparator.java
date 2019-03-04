package teckvillage.developer.khaled_pc.teckvillagetrue.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import teckvillage.developer.khaled_pc.teckvillagetrue.Model.UserContactData;

/**
 * Created by khaled-pc on 2/13/2019.
 */

public class LetterComparator  {

        public ArrayList<UserContactData> sortList(ArrayList<UserContactData> list) {
                try {
                        Collections.sort(list, new Comparator<UserContactData>() {
                                @Override
                                public int compare(UserContactData usercontact1, UserContactData usercontact2) {
                                        return usercontact1.usercontacName.substring(0, 1).toUpperCase().compareTo(usercontact2.usercontacName.substring(0, 1).toUpperCase());
                                }
                        });
                        return list;
                }
                catch (Exception e) {
                   e.printStackTrace();
                   return list;
                }
        }


        }
