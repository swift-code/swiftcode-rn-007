package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by lubuntu on 10/22/16.
 */
@Entity
public class Profile extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String firstname;
    public String company;
    public String lasttname;
    public static Model.Finder<Long,Profile> find = new Model.Finder<Long,Profile>(Profile.class);

    public Profile(String firstname, String lasttname) {
        this.firstname = firstname;
        this.lasttname = lasttname;
    }
}


