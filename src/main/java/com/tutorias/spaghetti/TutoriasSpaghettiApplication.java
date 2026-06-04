package com.tutorias.spaghetti;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TutoriasSpaghettiApplication {

    public static void main(String[] args) {
        System.out.println("Iniciando servidor de Tutorias UCE (Version Spaghetti)...");

        // 1. Cargar configuracion desde application.properties
        Properties props = new Properties();
        try (InputStream input = TutoriasSpaghettiApplication.class.getResourceAsStream("/application.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                // Fallback si se ejecuta fuera de classpath
                File file = new File("src/main/resources/application.properties");
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        props.load(fis);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("No se pudo cargar application.properties, usando valores por defecto: " + ex.getMessage());
        }

        String mongoUri = props.getProperty("spring.data.mongodb.uri", "mongodb://localhost:27017");
        String dbName = props.getProperty("spring.data.mongodb.database", "tutorias_Spaghetti.db");
        int port = Integer.parseInt(props.getProperty("server.port", "8080"));

        System.out.println("Conectando a MongoDB en: " + mongoUri);
        System.out.println("Base de datos: " + dbName);

        try {
            // 2. Inicializar cliente de MongoDB
            MongoClient mongoClient = MongoClients.create(mongoUri);
            MongoDatabase database = mongoClient.getDatabase(dbName);

            // 3. Sembrar datos semilla si está vacía
            sembrarDatosSemilla(database);

            // 4. Levantar servidor HTTP integrado en JDK
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new MonolithicHandler(database));
            server.setExecutor(null); // usar executor por defecto

            System.out.println("Servidor web listo en http://localhost:" + port + "/index.html");
            server.start();

        } catch (Exception e) {
            System.err.println("Error critico al iniciar el servidor:");
            e.printStackTrace();
        }
    }

    // ================= SEED DATA LOADER =================
    private static void sembrarDatosSemilla(MongoDatabase db) {
        MongoCollection<Document> usuariosCol = db.getCollection("usuarios");
        if (usuariosCol.countDocuments() == 0) {
            System.out.println("Coleccion 'usuarios' vacia. Cargando datos semilla...");

            // Materias
            MongoCollection<Document> materiasCol = db.getCollection("materias");
            materiasCol.deleteMany(new Document());
            materiasCol.insertOne(new Document("_id", "1").append("codigo", "AS-001").append("nombre", "Arquitectura de Software").append("estado", "Activo"));
            materiasCol.insertOne(new Document("_id", "2").append("codigo", "BD-002").append("nombre", "Base de Datos").append("estado", "Activo"));
            materiasCol.insertOne(new Document("_id", "3").append("codigo", "RC-003").append("nombre", "Redes de Computadoras").append("estado", "Activo"));

            // Usuarios
            usuariosCol.insertOne(new Document("_id", "u1").append("nombre", "Admin").append("apellido", "General").append("correoInstitucional", "admin@uce.edu.ec").append("contrasena", "admin123").append("rol", "administrador").append("estado", "Activo").append("fechaCreacion", "2026-06-03 12:00:00"));
            usuariosCol.insertOne(new Document("_id", "u2").append("nombre", "Carlos").append("apellido", "Perez").append("correoInstitucional", "docente1@uce.edu.ec").append("contrasena", "docente123").append("rol", "docente").append("estado", "Activo").append("fechaCreacion", "2026-06-03 12:00:00"));
            usuariosCol.insertOne(new Document("_id", "u3").append("nombre", "Maria").append("apellido", "Flores").append("correoInstitucional", "docente2@uce.edu.ec").append("contrasena", "docente123").append("rol", "docente").append("estado", "Activo").append("fechaCreacion", "2026-06-03 12:00:00"));
            usuariosCol.insertOne(new Document("_id", "u4").append("nombre", "Antony").append("apellido", "Coello").append("correoInstitucional", "estudiante1@uce.edu.ec").append("contrasena", "estudiante123").append("rol", "estudiante").append("estado", "Activo").append("fechaCreacion", "2026-06-03 12:00:00"));
            usuariosCol.insertOne(new Document("_id", "u5").append("nombre", "Juan").append("apellido", "Lopez").append("correoInstitucional", "estudiante2@uce.edu.ec").append("contrasena", "estudiante123").append("rol", "estudiante").append("estado", "Activo").append("fechaCreacion", "2026-06-03 12:00:00"));
            usuariosCol.insertOne(new Document("_id", "u6").append("nombre", "Inactivo").append("apellido", "Prueba").append("correoInstitucional", "inactivo@uce.edu.ec").append("contrasena", "inactivo123").append("rol", "estudiante").append("estado", "Inactivo").append("fechaCreacion", "2026-06-03 12:00:00"));

            // Docentes
            MongoCollection<Document> docentesCol = db.getCollection("docentes");
            docentesCol.deleteMany(new Document());
            docentesCol.insertOne(new Document("_id", "d1").append("usuarioId", "u2").append("departamento", "Computacion"));
            docentesCol.insertOne(new Document("_id", "d2").append("usuarioId", "u3").append("departamento", "Redes"));

            // Estudiantes
            MongoCollection<Document> estudiantesCol = db.getCollection("estudiantes");
            estudiantesCol.deleteMany(new Document());
            estudiantesCol.insertOne(new Document("_id", "e1").append("usuarioId", "u4").append("carrera", "Computacion").append("nivel", "7").append("paralelo", "A"));
            estudiantesCol.insertOne(new Document("_id", "e2").append("usuarioId", "u5").append("carrera", "Computacion").append("nivel", "7").append("paralelo", "B"));
            estudiantesCol.insertOne(new Document("_id", "e3").append("usuarioId", "u6").append("carrera", "Computacion").append("nivel", "1").append("paralelo", "A"));

            // disponibilidades
            MongoCollection<Document> dispCol = db.getCollection("disponibilidades");
            dispCol.deleteMany(new Document());
            dispCol.insertOne(new Document("_id", "disp1").append("docenteId", "d1").append("docenteNombre", "Carlos Perez").append("materiaId", "1").append("materiaNombre", "Arquitectura de Software").append("fecha", "2026-06-10").append("horaInicio", "09:00").append("horaFin", "10:00").append("estado", "Disponible"));
            dispCol.insertOne(new Document("_id", "disp2").append("docenteId", "d1").append("docenteNombre", "Carlos Perez").append("materiaId", "2").append("materiaNombre", "Base de Datos").append("fecha", "2026-06-11").append("horaInicio", "11:00").append("horaFin", "12:00").append("estado", "Disponible"));
            dispCol.insertOne(new Document("_id", "disp3").append("docenteId", "d2").append("docenteNombre", "Maria Flores").append("materiaId", "3").append("materiaNombre", "Redes de Computadoras").append("fecha", "2026-06-12").append("horaInicio", "14:00").append("horaFin", "15:00").append("estado", "Disponible"));

            // Limpiar otras colecciones
            db.getCollection("tutorias").deleteMany(new Document());
            db.getCollection("historial_cambios").deleteMany(new Document());
            db.getCollection("notificaciones").deleteMany(new Document());

            System.out.println("Datos semilla sembrados exitosamente.");
        }
    }

    // ================= MONOLITHIC REQUEST HANDLER =================
    private static class MonolithicHandler implements HttpHandler {
        private final MongoDatabase db;

        public MonolithicHandler(MongoDatabase db) {
            this.db = db;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            System.out.println("Peticion recibida: " + method + " " + path);

            // CORS preflight
            if (method.equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                // --- RUTA: SERVIR CLIENTE ESTATICO ---
                if (path.equals("/") || path.equals("/index.html")) {
                    InputStream htmlStream = MonolithicHandler.class.getResourceAsStream("/static/index.html");
                    if (htmlStream == null) {
                        // Fallback si no está empaquetado en resources
                        File file = new File("src/main/resources/static/index.html");
                        if (file.exists()) {
                            htmlStream = new FileInputStream(file);
                        }
                    }
                    if (htmlStream == null) {
                        sendResponse(exchange, 404, "text/plain", "Archivo index.html no encontrado.");
                        return;
                    }
                    byte[] htmlBytes = htmlStream.readAllBytes();
                    htmlStream.close();
                    sendResponse(exchange, 200, "text/html; charset=utf-8", htmlBytes);
                    return;
                }

                // --- RUTA: LOGIN ---
                if (path.equals("/api/auth/login") && method.equals("POST")) {
                    String json = readRequestBody(exchange);
                    String correo = getJsonVal(json, "correoInstitucional");
                    String contrasena = getJsonVal(json, "contrasena");

                    if (correo == null || !correo.endsWith("@uce.edu.ec")) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El correo debe ser institucional (@uce.edu.ec)\"}");
                        return;
                    }

                    Document user = db.getCollection("usuarios").find(Filters.eq("correoInstitucional", correo)).first();

                    if (user == null || !user.getString("contrasena").equals(contrasena)) {
                        sendResponse(exchange, 401, "application/json", "{\"message\":\"Credenciales incorrectas\"}");
                        return;
                    }

                    if ("Inactivo".equals(user.getString("estado"))) {
                        sendResponse(exchange, 403, "application/json", "{\"message\":\"No se permite el acceso a cuentas inactivas\"}");
                        return;
                    }

                    sendResponse(exchange, 200, "application/json", serializeDoc(user));
                    return;
                }

                // --- RUTA: LISTAR USUARIOS ---
                if (path.equals("/api/usuarios") && method.equals("GET")) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
                    String rol = query.get("rol");

                    List<Document> list = new ArrayList<>();
                    Bson filter = rol != null && !rol.isEmpty() ? Filters.eq("rol", rol) : new Document();
                    db.getCollection("usuarios").find(filter).into(list);

                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // --- RUTA: CREAR USUARIO (ADMIN) ---
                if (path.equals("/api/usuarios") && method.equals("POST")) {
                    String json = readRequestBody(exchange);
                    String nombre = getJsonVal(json, "nombre");
                    String apellido = getJsonVal(json, "apellido");
                    String correo = getJsonVal(json, "correoInstitucional");
                    String contrasena = getJsonVal(json, "contrasena");
                    String rol = getJsonVal(json, "rol");
                    String estado = getJsonVal(json, "estado");

                    if (correo == null || !correo.endsWith("@uce.edu.ec")) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El correo debe ser institucional (@uce.edu.ec)\"}");
                        return;
                    }

                    Document existente = db.getCollection("usuarios").find(Filters.eq("correoInstitucional", correo)).first();
                    if (existente != null) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El correo ya está registrado\"}");
                        return;
                    }

                    String userId = UUID.randomUUID().toString();
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    Document nuevoUsuario = new Document("_id", userId)
                            .append("nombre", nombre)
                            .append("apellido", apellido)
                            .append("correoInstitucional", correo)
                            .append("contrasena", contrasena)
                            .append("rol", rol)
                            .append("estado", estado)
                            .append("fechaCreacion", timestamp);

                    db.getCollection("usuarios").insertOne(nuevoUsuario);

                    if ("estudiante".equals(rol)) {
                        Document estudiante = new Document("_id", UUID.randomUUID().toString())
                                .append("usuarioId", userId)
                                .append("carrera", getJsonVal(json, "carrera"))
                                .append("nivel", getJsonVal(json, "nivel"))
                                .append("paralelo", getJsonVal(json, "paralelo"));
                        db.getCollection("estudiantes").insertOne(estudiante);
                    } else if ("docente".equals(rol)) {
                        Document docente = new Document("_id", UUID.randomUUID().toString())
                                .append("usuarioId", userId)
                                .append("departamento", getJsonVal(json, "departamento"));
                        db.getCollection("docentes").insertOne(docente);
                    }

                    sendResponse(exchange, 200, "application/json", serializeDoc(nuevoUsuario));
                    return;
                }

                // --- RUTA: ACTIVAR/DESACTIVAR USUARIO ---
                if (path.startsWith("/api/usuarios/") && path.endsWith("/toggle") && method.equals("PUT")) {
                    String id = path.substring("/api/usuarios/".length(), path.length() - "/toggle".length());

                    Document user = db.getCollection("usuarios").find(Filters.eq("_id", id)).first();
                    if (user == null) {
                        sendResponse(exchange, 404, "application/json", "{\"message\":\"Usuario no encontrado\"}");
                        return;
                    }

                    String nuevoEstado = "Activo".equals(user.getString("estado")) ? "Inactivo" : "Activo";
                    db.getCollection("usuarios").updateOne(Filters.eq("_id", id), new Document("$set", new Document("estado", nuevoEstado)));
                    
                    user.put("estado", nuevoEstado);
                    sendResponse(exchange, 200, "application/json", serializeDoc(user));
                    return;
                }

                // --- RUTA: OBTENER PERFIL ESTUDIANTE POR USUARIO_ID ---
                if (path.startsWith("/api/estudiantes/usuario/") && method.equals("GET")) {
                    String userId = path.substring("/api/estudiantes/usuario/".length());

                    Document student = db.getCollection("estudiantes").find(Filters.eq("usuarioId", userId)).first();
                    if (student == null) {
                        sendResponse(exchange, 404, "application/json", "{\"message\":\"Estudiante no encontrado\"}");
                        return;
                    }

                    sendResponse(exchange, 200, "application/json", serializeDoc(student));
                    return;
                }

                // --- RUTA: OBTENER PERFIL DOCENTE POR USUARIO_ID ---
                if (path.startsWith("/api/docentes/usuario/") && method.equals("GET")) {
                    String userId = path.substring("/api/docentes/usuario/".length());

                    Document teacher = db.getCollection("docentes").find(Filters.eq("usuarioId", userId)).first();
                    if (teacher == null) {
                        sendResponse(exchange, 404, "application/json", "{\"message\":\"Docente no encontrado\"}");
                        return;
                    }

                    sendResponse(exchange, 200, "application/json", serializeDoc(teacher));
                    return;
                }

                // --- RUTA: LISTAR MATERIAS ---
                if (path.equals("/api/materias") && method.equals("GET")) {
                    List<Document> list = new ArrayList<>();
                    db.getCollection("materias").find().into(list);
                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // --- RUTA: LISTAR DISPONIBILIDADES ---
                if (path.equals("/api/disponibilidades") && method.equals("GET")) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
                    String estado = query.get("estado");
                    String docenteId = query.get("docenteId");
                    String materiaId = query.get("materiaId");
                    String fecha = query.get("fecha");

                    List<Bson> filters = new ArrayList<>();
                    if (estado != null && !estado.isEmpty()) filters.add(Filters.eq("estado", estado));
                    if (docenteId != null && !docenteId.isEmpty()) filters.add(Filters.eq("docenteId", docenteId));
                    if (materiaId != null && !materiaId.isEmpty()) filters.add(Filters.eq("materiaId", materiaId));
                    if (fecha != null && !fecha.isEmpty()) filters.add(Filters.eq("fecha", fecha));

                    Bson finalFilter = filters.isEmpty() ? new Document() : Filters.and(filters);
                    List<Document> list = new ArrayList<>();
                    db.getCollection("disponibilidades").find(finalFilter).into(list);

                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // --- RUTA: CREAR DISPONIBILIDAD (DOCENTE) ---
                if (path.equals("/api/disponibilidades") && method.equals("POST")) {
                    String json = readRequestBody(exchange);
                    String docenteId = getJsonVal(json, "docenteId");
                    String materiaId = getJsonVal(json, "materiaId");
                    String fecha = getJsonVal(json, "fecha");
                    String horaInicio = getJsonVal(json, "horaInicio");
                    String horaFin = getJsonVal(json, "horaFin");

                    // Rango de horas
                    try {
                        LocalTime inicio = LocalTime.parse(horaInicio);
                        LocalTime fin = LocalTime.parse(horaFin);
                        if (!inicio.isBefore(fin)) {
                            sendResponse(exchange, 400, "application/json", "{\"message\":\"La hora de inicio debe ser anterior a la hora de fin\"}");
                            return;
                        }
                    } catch (Exception e) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"Formato de hora incorrecto (HH:MM)\"}");
                        return;
                    }

                    // Regla: No solapamiento de disponibilidades del docente
                    List<Document> existentes = new ArrayList<>();
                    db.getCollection("disponibilidades").find(
                            Filters.and(
                                    Filters.eq("docenteId", docenteId),
                                    Filters.eq("fecha", fecha),
                                    Filters.eq("estado", "Disponible")
                            )
                    ).into(existentes);

                    LocalTime nuevoInicio = LocalTime.parse(horaInicio);
                    LocalTime nuevoFin = LocalTime.parse(horaFin);

                    for (Document ext : existentes) {
                        LocalTime extInicio = LocalTime.parse(ext.getString("horaInicio"));
                        LocalTime extFin = LocalTime.parse(ext.getString("horaFin"));

                        if (nuevoInicio.isBefore(extFin) && nuevoFin.isAfter(extInicio)) {
                            sendResponse(exchange, 400, "application/json", "{\"message\":\"El horario se solapa con una disponibilidad ya registrada para este docente\"}");
                            return;
                        }
                    }

                    // Denormalizar nombres
                    String docenteNombre = "Docente";
                    Document docente = db.getCollection("docentes").find(Filters.eq("_id", docenteId)).first();
                    if (docente != null) {
                        Document u = db.getCollection("usuarios").find(Filters.eq("_id", docente.getString("usuarioId"))).first();
                        if (u != null) docenteNombre = u.getString("nombre") + " " + u.getString("apellido");
                    }

                    String materiaNombre = "Materia";
                    Document materia = db.getCollection("materias").find(Filters.eq("_id", materiaId)).first();
                    if (materia != null) materiaNombre = materia.getString("nombre");

                    String dispId = UUID.randomUUID().toString();
                    Document newDisp = new Document("_id", dispId)
                            .append("docenteId", docenteId)
                            .append("docenteNombre", docenteNombre)
                            .append("materiaId", materiaId)
                            .append("materiaNombre", materiaNombre)
                            .append("fecha", fecha)
                            .append("horaInicio", horaInicio)
                            .append("horaFin", horaFin)
                            .append("estado", "Disponible");

                    db.getCollection("disponibilidades").insertOne(newDisp);

                    sendResponse(exchange, 200, "application/json", serializeDoc(newDisp));
                    return;
                }

                // --- RUTA: ELIMINAR DISPONIBILIDAD (DOCENTE) ---
                if (path.startsWith("/api/disponibilidades/") && method.equals("DELETE")) {
                    String id = path.substring("/api/disponibilidades/".length());

                    Document disp = db.getCollection("disponibilidades").find(Filters.eq("_id", id)).first();
                    if (disp == null) {
                        sendResponse(exchange, 404, "application/json", "{\"message\":\"Disponibilidad no encontrada\"}");
                        return;
                    }

                    if ("Reservado".equals(disp.getString("estado"))) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"Un horario ya reservado no puede ser eliminado libremente\"}");
                        return;
                    }

                    db.getCollection("disponibilidades").deleteOne(Filters.eq("_id", id));
                    sendResponse(exchange, 200, "application/json", "{\"success\":true}");
                    return;
                }

                // --- RUTA: LISTAR TUTORÍAS ---
                if (path.equals("/api/tutorias") && method.equals("GET")) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
                    String estudianteId = query.get("estudianteId");
                    String docenteId = query.get("docenteId");
                    String estado = query.get("estado");
                    String estudianteNombre = query.get("estudianteNombre");
                    String docenteNombre = query.get("docenteNombre");

                    List<Bson> filters = new ArrayList<>();
                    if (estudianteId != null && !estudianteId.isEmpty()) filters.add(Filters.eq("estudianteId", estudianteId));
                    if (docenteId != null && !docenteId.isEmpty()) filters.add(Filters.eq("docenteId", docenteId));
                    if (estado != null && !estado.isEmpty()) filters.add(Filters.eq("estado", estado));
                    if (estudianteNombre != null && !estudianteNombre.isEmpty()) {
                        filters.add(Filters.regex("estudianteNombre", estudianteNombre, "i"));
                    }
                    if (docenteNombre != null && !docenteNombre.isEmpty()) {
                        filters.add(Filters.regex("docenteNombre", docenteNombre, "i"));
                    }

                    Bson finalFilter = filters.isEmpty() ? new Document() : Filters.and(filters);
                    List<Document> list = new ArrayList<>();
                    db.getCollection("tutorias").find(finalFilter).into(list);

                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // --- RUTA: AGENDAR TUTORÍA (ESTUDIANTE) ---
                if (path.equals("/api/tutorias") && method.equals("POST")) {
                    String json = readRequestBody(exchange);
                    String estudianteId = getJsonVal(json, "estudianteId");
                    String disponibilidadId = getJsonVal(json, "disponibilidadId");
                    String motivo = getJsonVal(json, "motivo");

                    if (estudianteId == null || disponibilidadId == null || motivo == null || motivo.trim().isEmpty()) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"Campos obligatorios faltantes\"}");
                        return;
                    }

                    Document disp = db.getCollection("disponibilidades").find(Filters.eq("_id", disponibilidadId)).first();
                    if (disp == null || !"Disponible".equals(disp.getString("estado"))) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El horario seleccionado no existe o ya no está disponible\"}");
                        return;
                    }

                    Document estudiante = db.getCollection("estudiantes").find(Filters.eq("_id", estudianteId)).first();
                    if (estudiante == null) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El estudiante no existe\"}");
                        return;
                    }

                    Document estUsuario = db.getCollection("usuarios").find(Filters.eq("_id", estudiante.getString("usuarioId"))).first();
                    if (estUsuario == null || !"Activo".equals(estUsuario.getString("estado"))) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"El estudiante no se encuentra activo en el sistema\"}");
                        return;
                    }

                    // Regla: No agendar en fecha pasada
                    String fechaHoraStr = disp.getString("fecha") + " " + disp.getString("horaInicio");
                    LocalDateTime fechaHoraTutoria = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    if (fechaHoraTutoria.isBefore(LocalDateTime.now())) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"No se puede agendar una tutoría en una fecha u hora pasada\"}");
                        return;
                    }

                    // Regla: Duplicado semanal
                    if (esDuplicadoSemanal(estudianteId, disp.getString("docenteId"), disp.getString("materiaId"), disp.getString("fecha"))) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"Ya has agendado una tutoría con este docente para esta materia esta misma semana\"}");
                        return;
                    }

                    // Marcar Reservado
                    db.getCollection("disponibilidades").updateOne(Filters.eq("_id", disponibilidadId), new Document("$set", new Document("estado", "Reservado")));

                    String tutoriaId = UUID.randomUUID().toString();
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    Document tutoria = new Document("_id", tutoriaId)
                            .append("estudianteId", estudianteId)
                            .append("estudianteNombre", estUsuario.getString("nombre") + " " + estUsuario.getString("apellido"))
                            .append("docenteId", disp.getString("docenteId"))
                            .append("docenteNombre", disp.getString("docenteNombre"))
                            .append("materiaId", disp.getString("materiaId"))
                            .append("materiaNombre", disp.getString("materiaNombre"))
                            .append("disponibilidadId", disponibilidadId)
                            .append("fechaHoraInicio", fechaHoraStr)
                            .append("fechaHoraFin", disp.getString("fecha") + " " + disp.getString("horaFin"))
                            .append("motivo", motivo)
                            .append("estado", "Pendiente")
                            .append("fechaCreacion", timestamp)
                            .append("fechaActualizacion", timestamp);

                    db.getCollection("tutorias").insertOne(tutoria);

                    // Historial
                    registrarHistorial(tutoriaId, null, "Pendiente", "Creación de solicitud de tutoría académica", timestamp, estUsuario.getString("correoInstitucional"));

                    // Notificacion
                    registrarNotificacion(tutoriaId, estUsuario.getString("correoInstitucional"), "creacion",
                            "Tu tutoría de " + disp.getString("materiaNombre") + " con el Doc. " + disp.getString("docenteNombre") + " el " + disp.getString("fecha") + " a las " + disp.getString("horaInicio") + " ha sido solicitada. Estado: Pendiente.",
                            timestamp);

                    sendResponse(exchange, 200, "application/json", serializeDoc(tutoria));
                    return;
                }

                // --- RUTA: CAMBIO DE ESTADOS Y CANCELACIÓN ---
                if (path.startsWith("/api/tutorias/") && path.endsWith("/status") && method.equals("PUT")) {
                    String id = path.substring("/api/tutorias/".length(), path.length() - "/status".length());
                    String json = readRequestBody(exchange);
                    String nuevoEstado = getJsonVal(json, "nuevoEstado");
                    String observacion = getJsonVal(json, "observacion");
                    String usuarioResponsable = getJsonVal(json, "usuarioResponsable"); // Email

                    Document tutoria = db.getCollection("tutorias").find(Filters.eq("_id", id)).first();
                    if (tutoria == null) {
                        sendResponse(exchange, 404, "application/json", "{\"message\":\"Tutoria no encontrada\"}");
                        return;
                    }

                    String estadoAnterior = tutoria.getString("estado");

                    // Validar estado terminal
                    if ("Asistida".equals(estadoAnterior) || "Inasistencia".equals(estadoAnterior) || "Cancelada".equals(estadoAnterior)) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"No se permiten transiciones desde estados terminales (Asistida, Inasistencia, Cancelada)\"}");
                        return;
                    }

                    if ("Confirmada".equals(nuevoEstado) && "Cancelada".equals(estadoAnterior)) {
                        sendResponse(exchange, 400, "application/json", "{\"message\":\"Una tutoría cancelada no puede volver a confirmarse sin crear otra solicitud\"}");
                        return;
                    }

                    // Regla: Cancelacion con 24h minimo
                    if ("Cancelada".equals(nuevoEstado)) {
                        Document uResp = db.getCollection("usuarios").find(Filters.eq("correoInstitucional", usuarioResponsable)).first();
                        if (uResp != null && "estudiante".equals(uResp.getString("rol"))) {
                            LocalDateTime ahora = LocalDateTime.now();
                            LocalDateTime fechaHoraTutoria = LocalDateTime.parse(tutoria.getString("fechaHoraInicio"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                            if (ahora.plusHours(24).isAfter(fechaHoraTutoria)) {
                                sendResponse(exchange, 400, "application/json", "{\"message\":\"No puedes cancelar esta tutoría. Falta menos de 24 horas para su inicio.\"}");
                                return;
                            }
                        }
                    }

                    // Aplicar cambio
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    db.getCollection("tutorias").updateOne(Filters.eq("_id", id), new Document("$set", new Document("estado", nuevoEstado).append("observacion", observacion).append("fechaActualizacion", timestamp)));

                    // Si se canceló, liberar la disponibilidad
                    if ("Cancelada".equals(nuevoEstado)) {
                        db.getCollection("disponibilidades").updateOne(Filters.eq("_id", tutoria.getString("disponibilidadId")), new Document("$set", new Document("estado", "Disponible")));
                    }

                    // Loggear auditoría
                    registrarHistorial(id, estadoAnterior, nuevoEstado, observacion, timestamp, usuarioResponsable);

                    // Loggear notificación al estudiante
                    Document estudiante = db.getCollection("estudiantes").find(Filters.eq("_id", tutoria.getString("estudianteId"))).first();
                    if (estudiante != null) {
                        Document uEst = db.getCollection("usuarios").find(Filters.eq("_id", estudiante.getString("usuarioId"))).first();
                        if (uEst != null) {
                            registrarNotificacion(id, uEst.getString("correoInstitucional"), "cambio_estado",
                                    "Tu tutoría de " + tutoria.getString("materiaNombre") + " cambio de estado a: " + nuevoEstado + ". Detalle: " + observacion,
                                    timestamp);
                        }
                    }

                    tutoria.put("estado", nuevoEstado);
                    tutoria.put("observacion", observacion);
                    sendResponse(exchange, 200, "application/json", serializeDoc(tutoria));
                    return;
                }

                // --- RUTA: HISTORIAL DE TRAZABILIDAD ---
                if (path.startsWith("/api/tutorias/") && path.endsWith("/historial") && method.equals("GET")) {
                    String id = path.substring("/api/tutorias/".length(), path.length() - "/historial".length());

                    List<Document> list = new ArrayList<>();
                    db.getCollection("historial_cambios").find(Filters.eq("tutoriaId", id)).into(list);

                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // --- RUTA: LISTAR NOTIFICACIONES ---
                if (path.equals("/api/notificaciones") && method.equals("GET")) {
                    Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
                    String dest = query.get("destinatario");

                    List<Document> list = new ArrayList<>();
                    if (dest != null && !dest.isEmpty()) {
                        db.getCollection("notificaciones").find(Filters.eq("destinatario", dest)).into(list);
                    }

                    sendResponse(exchange, 200, "application/json", serializeList(list));
                    return;
                }

                // Ruta no encontrada
                sendResponse(exchange, 404, "application/json", "{\"message\":\"Ruta no encontrada\"}");

            } catch (Exception e) {
                System.err.println("Error procesando solicitud:");
                e.printStackTrace();
                sendResponse(exchange, 500, "application/json", "{\"message\":\"Internal Server Error: " + e.getMessage() + "\"}");
            }
        }

        // ================= HELPER FUNCTIONS =================

        private String readRequestBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        private String getJsonVal(String json, String key) {
            if (json == null || json.isEmpty()) return null;
            // Intentar con comillas
            Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1);
            }
            // Intentar sin comillas (para booleanos o números)
            Pattern patternNum = Pattern.compile("\"" + key + "\"\\s*:\\s*([^,\\}\\s]+)");
            Matcher matcherNum = patternNum.matcher(json);
            if (matcherNum.find()) {
                return matcherNum.group(1).trim().replace("\"", "");
            }
            return null;
        }

        private Map<String, String> parseQueryParams(String query) {
            Map<String, String> params = new HashMap<>();
            if (query == null || query.isEmpty()) return params;
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    params.put(entry[0], URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
                } else if (entry.length == 1) {
                    params.put(entry[0], "");
                }
            }
            return params;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String contentType, String content) throws IOException {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            sendResponse(exchange, statusCode, contentType, bytes);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String contentType, byte[] bytes) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String serializeDoc(Document doc) {
            if (doc == null) return "{}";
            Document copy = new Document(doc);
            Object idVal = copy.get("_id");
            if (idVal != null) {
                copy.put("id", idVal.toString());
            }
            return copy.toJson();
        }

        private String serializeList(List<Document> list) {
            List<String> jsonList = new ArrayList<>();
            for (Document doc : list) {
                jsonList.add(serializeDoc(doc));
            }
            return "[" + String.join(",", jsonList) + "]";
        }

        private boolean esDuplicadoSemanal(String estudianteId, String docenteId, String materiaId, String fechaStr) {
            try {
                LocalDate fecha = LocalDate.parse(fechaStr);
                int semana = fecha.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                int anio = fecha.getYear();

                List<Document> tutorias = new ArrayList<>();
                db.getCollection("tutorias").find(
                        Filters.and(
                                Filters.eq("estudianteId", estudianteId),
                                Filters.eq("docenteId", docenteId),
                                Filters.eq("materiaId", materiaId),
                                Filters.ne("estado", "Cancelada")
                        )
                ).into(tutorias);

                for (Document t : tutorias) {
                    String datePart = t.getString("fechaHoraInicio").split(" ")[0];
                    LocalDate tFecha = LocalDate.parse(datePart);
                    int tSemana = tFecha.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                    int tAnio = tFecha.getYear();

                    if (semana == tSemana && anio == tAnio) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private void registrarHistorial(String tutoriaId, String anterior, String nuevo, String desc, String fecha, String usuario) {
            Document hc = new Document("_id", UUID.randomUUID().toString())
                    .append("tutoriaId", tutoriaId)
                    .append("estadoAnterior", anterior)
                    .append("estadoNuevo", nuevo)
                    .append("descripcion", desc)
                    .append("fechaEvento", fecha)
                    .append("usuarioResponsable", usuario);
            db.getCollection("historial_cambios").insertOne(hc);
        }

        private void registrarNotificacion(String tutoriaId, String emailDestinatario, String tipo, String msj, String fecha) {
            Document n = new Document("_id", UUID.randomUUID().toString())
                    .append("tutoriaId", tutoriaId)
                    .append("destinatario", emailDestinatario)
                    .append("tipo", tipo)
                    .append("mensaje", msj)
                    .append("estadoEnvio", "Enviado")
                    .append("fechaEnvio", fecha);
            db.getCollection("notificaciones").insertOne(n);
        }
    }
}
