package cartelera.turnodetarde.example.com;

import java.util.List;

/**
 * Created by turno de tarde on 07/07/2015.
 */
public class Channel {
    private String name;
    private int pos;
    private List<Program> programs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
