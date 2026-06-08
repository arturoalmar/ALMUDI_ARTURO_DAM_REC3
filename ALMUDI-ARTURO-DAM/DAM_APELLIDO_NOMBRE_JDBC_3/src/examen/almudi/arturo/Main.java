/*
=========================================
AUTOR: ARTURO ALMUDI MARCO
GRUPO: DAM3
EXAMEN JDBC AWS RDS
FECHA: 08/06/2026
=========================================
*/

package examen.almudi.arturo;

import examen.almudi.arturo.dao.SocDAOImpl;
import examen.almudi.arturo.dao.IncidenteDAOImpl;
import examen.almudi.arturo.dao.InformeIncidenteDAOImpl;
import examen.almudi.arturo.beans.Soc;
import examen.almudi.arturo.beans.Incidente;
import examen.almudi.arturo.motores.MotorFactory;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        IncidenteDAOImpl incidenteDAO = new IncidenteDAOImpl(
                MotorFactory.create(MotorFactory.POSTGRE));

        incidenteDAO.check();

        SocDAOImpl socDAO = new SocDAOImpl(
                MotorFactory.create(MotorFactory.POSTGRE));
        Soc testsoc = socDAO.findAll().get(0);

        // =============================================
        // TEST 1 — ADD INCIDENTE
        // =============================================
        Incidente test = new Incidente();
        test.setCodigo_incidente("INC-2026-TEST");
        test.setTipo_incidente("APAGON");
        test.setFecha_deteccion("02/06/2026");
        test.setEstado("EN_PROCESO");
        test.setSoc(testsoc);
        test.setAutorExamen("Arturo_Almudi_Marco_DAM3");
        incidenteDAO.add(test);
        System.out.println("[TEST 1] ADD -> OK");

        // =============================================
        // TEST 2 — UPDATE INCIDENTE
        // =============================================
        test.setEstado("CERRADO");
        incidenteDAO.update(1, test);
        System.out.println("[TEST 2] UPDATE -> OK");

        // =============================================
        // TEST 3 — FIND INCIDENTE
        // =============================================
        Incidente encontrado = incidenteDAO.find(1);
        System.out.println("[TEST 3] FIND -> " + encontrado);

        // =============================================
        // TEST 4 — FIND ALL INCIDENTES
        // =============================================
        ArrayList<Incidente> todos = incidenteDAO.findAll();
        System.out.println("[TEST 4] FIND ALL -> " + todos.size() + " registros");
        for (Incidente m : todos) {
            System.out.println(m);
        }

        // =============================================
        // TEST 5 — FIND INCIDENTES BY SOC
        // =============================================
        ArrayList<Incidente> porSoc = incidenteDAO.findBySoc(1);
        System.out.println("[TEST 5] BY SOC -> " + porSoc.size() + " registros");
        for (Incidente m : porSoc) {
            System.out.println(m);
        }

        // =============================================
        // TEST 6 — FIND INCIDENTE WITH INFORME
        // =============================================
        Incidente conInforme = incidenteDAO.findWithInforme(1);
        System.out.println("[TEST 6] WITH INFORME -> " + conInforme);

        // =============================================
        // BLOQUE 6 — BONUS_QUERY_ADVANCED: INCIDENTES_CRITICOS
        // =============================================
        ArrayList<Incidente> criticos = incidenteDAO.findIncidentesCriticos();
        System.out.println("[BLOQUE 6] INCIDENTES_CRITICOS -> " + criticos.size() + " registros");
        for (Incidente m : criticos) {
            System.out.println(m);
        }

        // =============================================
        // BLOQUE 7 — DYNAMIC_UPDATE_ENGINE
        // El sistema construye el SET dinamicamente segun los campos que se pasen.
        // Si un campo es null no se incluye en el UPDATE, evitando sobreescribir datos.
        // Esto permite actualizar solo los campos necesarios sin un metodo por campo.
        // El SQL se genera en AbstractDAO usando PreparedStatement para seguridad.
        // =============================================
        InformeIncidenteDAOImpl informeDAO = new InformeIncidenteDAOImpl(
                MotorFactory.create(MotorFactory.POSTGRE));

        // Ejemplo 1: actualizar solo nivel_severidad y conclusion (malware_detectado no se toca)
        informeDAO.updateDynamicInforme(1, null, 99, "ACTUALIZADO_DINAMICAMENTE");
        System.out.println("[BLOQUE 7] UPDATE dinamico (2 campos) sobre informe id=1 -> OK");

        // Ejemplo 2: actualizar solo un campo
        informeDAO.updateDynamicInforme(2, true, null, null);
        System.out.println("[BLOQUE 7] UPDATE dinamico (1 campo) sobre informe id=2 -> OK");

        // =============================================
        // BLOQUE 8 — ABSTRACTDAO_ARCHITECTURE_REFACTOR
        // ANALISIS: AbstractDAO solo debe contener logica comun a TODAS las entidades.
        // findIncidentesCriticos() vive en IncidenteDAOImpl (no en AbstractDAO) porque
        // solo tiene sentido para Incidente. Si estuviera en AbstractDAO o en DAO<T>
        // obligaria a SocDAOImpl e InformeIncidenteDAOImpl a implementarlo sin logica.
        // MEJORA: las consultas especializadas viven UNICAMENTE en el DAO concreto.
        // TEST: llamamos a findIncidentesCriticos() directamente sobre IncidenteDAOImpl,
        // demostrando que la arquitectura es correcta — no esta en la interfaz base.
        // =============================================
        ArrayList<Incidente> testArquitectura = incidenteDAO.findIncidentesCriticos();
        System.out.println("[BLOQUE 8] TEST ARQUITECTURA — findIncidentesCriticos() en IncidenteDAOImpl: "
                + testArquitectura.size() + " resultados");
        // SocDAOImpl y InformeIncidenteDAOImpl NO tienen este metodo — arquitectura correcta.
    }
}
