package servidor;

public class Tortugas {
    private String nombre;
    private int dorsal;

    public Tortugas(String nombre, int dorsal) {
        this.nombre = nombre;
        this.dorsal = dorsal;
    }

    public Tortugas() { }

    //geters
    public String getNombre() { return nombre; }
    public int getDorsal() { return dorsal; }

    //setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }

    @Override
    public String toString() {
        return "nombre='" + nombre + '\'' +
                " dorsal=" + dorsal + "\n";
    }


}

