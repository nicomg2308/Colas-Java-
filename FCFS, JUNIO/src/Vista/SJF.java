/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Vista.Ventana;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import SJF.Cola;
import SJF.Proceso;

/**
 *
 * AUTORES ALEJANDOR MORALES, NICOLÁS MENESES
 */
public class SJF extends Thread {

    public Cola listos;
    public Cola atendidos;
    public Cola bloqueados;
    private Ventana interfaz;

    public SJF(Cola cola, Ventana interfaz) {
        this.interfaz = interfaz;
        listos = new Cola(interfaz.getTxaConsola());
        this.listos = cola;
    }


    public void run() {

        atendidos = new Cola(interfaz.getTxaConsola());
        bloqueados=new Cola(interfaz.getTxaConsola());
        Proceso enEejecucion;
        Proceso enEejecucion1;
        listos.imprimir(interfaz.getjTableProcesos());
        int b=0;
        while ((listos.colaVacia() == false)) {
            try {
                sleep(250);
                if (listos.colaVacia() == false) {

                    enEejecucion = this.CompararPrioridad(listos.getCab().getSig());
                    if(enEejecucion.getProceso()==2){
                    	JOptionPane.showMessageDialog(null, "Se ha bloqueado el proceso "+enEejecucion.getProceso());
                    	sleep(2000);
                    	bloqueados.insertar(enEejecucion.getProceso(), enEejecucion.getTiempoInicial(), enEejecucion.getTiempoRafaga(), enEejecucion.getPrioridad());
                    	//b=enEejecucion.getRafagaRestante();
                    	listos.eliminarElemento(enEejecucion, listos);
                    }else{
                    	enEejecucion1 = this.CompararPrioridad(listos.getCab().getSig());
                    	if (enEejecucion1.getRafagaRestante() > 0) {
                            interfaz.getProcesos_en_ejecucion().setText(enEejecucion1.getProceso() + "");
                            int f = enEejecucion1.getRafagaRestante();
                            enEejecucion1.setTiempoEjecucion(Integer.parseInt(interfaz.getTiempo_real().getText()));
                            listos.imprimir(interfaz.getjTableProcesos());
                            for (int i = 1; i <= f; i++) {
                                sleep(250);
                                interfaz.getTiempo_real().setText((Integer.parseInt(interfaz.getTiempo_real().getText()) + 1) + "");
                                enEejecucion1.setTiempoEnCPU(enEejecucion1.getTiempoEnCPU() + 1);
                              //  interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                                interfaz.getProcesos_en_ejecucion().setText(enEejecucion1.getProceso() + "");
                                //interfaz.getCpu_nuevo().setText(enEejecucion.getRafagaRestante() + "");
                                enEejecucion1.setRafagaRestante(enEejecucion1.getRafagaRestante() - 1);

                                if (this.CompararPrioridad(enEejecucion1).getRafagaRestante() < enEejecucion1.getRafagaRestante()) {
                                    Proceso au1 = listos.buscarProceso(enEejecucion1);
                                    au1 = enEejecucion1;
                                    break;
                                }

                                for (int j = 1; j <= enEejecucion1.getTiempoInicial(); j++) {
                                    diagramaGantt(enEejecucion1, false, j);
                                }

                                listos.imprimir(interfaz.getjTableProcesos());
                                diagramaGantt(enEejecucion1, true, Integer.parseInt(interfaz.getTiempo_real().getText()));

                                if (enEejecucion1.getTiempoEnCPU() == enEejecucion1.getTiempoRafaga()) {

                                    

                                   // interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                                    sleep(1000);
                                    atendidos.insertar(enEejecucion1.getProceso(), enEejecucion1.getTiempoInicial(), enEejecucion1.getTiempoRafaga(), enEejecucion1.getPrioridad());
                                    Proceso aux = listos.buscarProceso(enEejecucion1);
                                    int tfinal = Integer.parseInt(interfaz.getTiempo_real().getText());
                                    aux.setTiempoFinal(tfinal);
                                    int tretorno = tfinal - enEejecucion.getTiempoInicial();
                                    aux.setTiempoRetorno(tretorno);
                                    int tespera = tretorno - enEejecucion.getTiempoRafaga();
                                    aux.setTiempoEespera(tespera);

                                    listos.imprimir(interfaz.getjTableProcesos());
                                    listos.eliminarElemento(enEejecucion1, listos);

                                    sleep(1000);//2000
                                    i = f;
                                 
                                    interfaz.getProcesos_en_ejecucion().setText("");
                                    
                                }

                            }

                        }
                    }
                    if (bloqueados.colaVacia()==false && listos.colaVacia()==true ) {
                    	JOptionPane.showMessageDialog(null, "probando la entrada");
                    	//listos.insertar(enEejecucion.getProceso(), enEejecucion.getTiempoInicial(), enEejecucion.getTiempoRafaga(), enEejecucion.getPrioridad());
                    	//enEejecucion.setRafagaRestante(b);
                   		 if (enEejecucion.getRafagaRestante() > 0) {
                   			JOptionPane.showMessageDialog(null, "probando la entrada1");
                             interfaz.getProcesos_en_ejecucion().setText(enEejecucion.getProceso() + "");
                             int f = enEejecucion.getRafagaRestante();
                             enEejecucion.setTiempoEjecucion(Integer.parseInt(interfaz.getTiempo_real().getText()));
                             bloqueados.imprimir(interfaz.getjTableProcesos());
                             for (int i = 1; i <= f; i++) {
                                 sleep(250);
                                 interfaz.getTiempo_real().setText((Integer.parseInt(interfaz.getTiempo_real().getText()) + 1) + "");
                                 enEejecucion.setTiempoEnCPU(enEejecucion.getTiempoEnCPU() + 1);
                               //  interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                                 interfaz.getProcesos_en_ejecucion().setText(enEejecucion.getProceso() + "");
                                 //interfaz.getCpu_nuevo().setText(enEejecucion.getRafagaRestante() + "");
                                 enEejecucion.setRafagaRestante(enEejecucion.getRafagaRestante() - 1);

                                

                                 for (int j = 1; j <= enEejecucion.getTiempoInicial(); j++) {
                                     diagramaGantt(enEejecucion, false, j);
                                 }

                                 bloqueados.imprimir(interfaz.getjTableProcesos());
                                 diagramaGantt(enEejecucion, true, Integer.parseInt(interfaz.getTiempo_real().getText()));

                                 if (enEejecucion.getTiempoEnCPU() == enEejecucion.getTiempoRafaga()) {

                                     

                                    // interfaz.getTiempo_cpu().setText(enEejecucion.getTiempoEnCPU() + "");
                                     sleep(1000);
                                     atendidos.insertar(enEejecucion.getProceso(), enEejecucion.getTiempoInicial(), enEejecucion.getTiempoRafaga(), enEejecucion.getPrioridad());
                                     Proceso aux = bloqueados.buscarProceso(enEejecucion);
                                     int tfinal = Integer.parseInt(interfaz.getTiempo_real().getText());
                                     aux.setTiempoFinal(tfinal);
                                     int tretorno = tfinal - enEejecucion.getTiempoInicial();
                                     aux.setTiempoRetorno(tretorno);
                                     int tespera = tretorno - enEejecucion.getTiempoRafaga();
                                     aux.setTiempoEespera(tespera+2);

                                     bloqueados.imprimir(interfaz.getjTableProcesos());
                                     //listos.imprimir(interfaz.getjTableProcesos());
                                     bloqueados.eliminarElemento(enEejecucion, bloqueados);
                                     //listos.eliminarElemento(enEejecucion, listos);
                                     //listos.eliminarElemento(enEejecucion, listos);
                                     sleep(1000);//2000
                                     i = f;
                                  
                                     interfaz.getProcesos_en_ejecucion().setText("");
                                     
                                 }

                             }

                         }
                   		 
                   	 
						
					}
                    
                   
                   
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(SJF.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        JOptionPane.showMessageDialog(null, "Ejecución Finalizada");

    }

    public Proceso CompararPrioridad(Proceso entrante) {

        Proceso comparado = entrante;
        Proceso aux_inicial = listos.getCab().getSig();
        while (aux_inicial != null) {
            if (entrante.getPrioridad() > aux_inicial.getPrioridad()) {
                comparado = aux_inicial;
            }
            aux_inicial = aux_inicial.getSig();
        }
        return comparado;
    }

    public void diagramaGantt(Proceso actual, boolean estado, int transcurrido) {//actualiza diagrama de gantt desde la interfaz grafica
        String fase;
        if (estado) {
            fase = "X";
        } else {
            fase = "E";
        }
        interfaz.getjTable1().setValueAt(fase, actual.getProceso() - 1, transcurrido);
        interfaz.getjTable1().setDefaultRenderer(Object.class, new Gantt1());
    }

  

    int conteo_tiempo = 0;//variable usada para graficar el primer diagrama de gantt
    String diagramaGant1 = "";//variable usada para graficar el primer diagrama de gantt
    String diagramaGant2 = "0";//variable usada para graficar el segundo diagrama de gantt
}
