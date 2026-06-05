package ec.edu.uce.tutorias.application.dto;

public class DisponibilidadResponse {
    private String id;
    private String docenteId;
    private String materiaId;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private boolean reservado;

    public DisponibilidadResponse() {}

    public DisponibilidadResponse(String id, String docenteId, String materiaId, String fecha, String horaInicio, String horaFin, boolean reservado) {
        this.id = id;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.reservado = reservado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDocenteId() { return docenteId; }
    public void setDocenteId(String docenteId) { this.docenteId = docenteId; }
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public boolean isReservado() { return reservado; }
    public void setReservado(boolean reservado) { this.reservado = reservado; }
}
