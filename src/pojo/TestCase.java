/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thaina
 */
public class TestCase {

    private long id;
    private List<Mutant> killedMutants = new ArrayList<>();

    public TestCase(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Mutant> getKilledMutants() {
        return killedMutants;
    }

    public void setKilledMutants(List<Mutant> killedMutants) {
        this.killedMutants = killedMutants;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestCase other = (TestCase) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
