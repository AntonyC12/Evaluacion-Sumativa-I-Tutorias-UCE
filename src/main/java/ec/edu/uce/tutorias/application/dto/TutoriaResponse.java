package ec.edu.uce.tutorias.application.dto;

public class TutoriaResponse {
    private String id;
    private String estudianteId;
    private String docenteId;
    private String materiaId;
    private String disponibilidadId;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private String motivo;
    private String estado;

    public TutoriaResponse() {}

    public TutoriaResponse(String id, String estudianteId, String docenteId, String materiaId, String disponibilidadId, 
                           String fechaHoraInicio, String fechaHoraFin, String motivo, String estado) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
        this.disponibilidadId = disponibilidadId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.motivo = motivo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    public String getDocenteId() { return docenteId; }
    public void setDocenteId(String docenteId) { this.docenteId = docenteId; }
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public String getDisponibilidadId() { return disponibilidadId; }
    public void setDisponibilidadId(String disponibilidadId) { this.disponibilidadId = disponibilidadId; }
    public String getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(String fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public String getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(String fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
