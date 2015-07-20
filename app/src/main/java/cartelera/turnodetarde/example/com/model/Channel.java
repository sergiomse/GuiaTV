package cartelera.turnodetarde.example.com.model;

import java.util.List;

import cartelera.turnodetarde.example.com.Program;

/**
 * Created by turno de tarde on 07/07/2015.
 */
public class Channel implements Comparable<Channel>{

    private int id;
    private String name;
    private int pos;
    private List<Program> programs;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public int compareTo(Channel another) {
        return pos - another.pos;
    }
}
