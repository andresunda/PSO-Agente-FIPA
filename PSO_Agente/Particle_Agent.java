/**
 * AGENTE DE LA PARTICULA UNICA YA QUE SOLO TRABAJO EL PSO COMO AGENTE UNICO
 * Y BUSQUE LA COMUNICACION ENTRE ELLAS (LAS PARTICULAS)ENTONCES ESTE SE ENCARGAR DE REALICAR EL ALGORITMOS POR
 * LA COMUNICACION ENTRE ELLOS Y ASI EVLAUAR LA CADENA DE TEXTO QUE ES "PSO METHOD" PARA ASI EJECUTAR EL ALGORITMO Y ENCONTRAR EL GLOBAL BEST
 * IMPLEMENTANDOLE ASI EL PROTOCOLO DE FIPA APOYANDOME DEL BOOK TRADE AGENT COMO EJEMPLO VIENDO LOS PROTOCOLOS Y IMPLEMENTARLO EN EL PSO
 */
import jade.core.Agent;
import jade.core.AID;                     // Esta clase representa un identificador de agente JADE. Las tablas de agentes internos de JADE utilizan esta clase para registrar nombres y direcciones de agentes.
import jade.lang.acl.ACLMessage;          //La clase ACLMessage implementa un mensaje ACL que cumple con las especificaciones FIPA 2000 "FIPA ACL Message Structure Specification" (fipa000061).
import jade.proto.ContractNetInitiator;  //Esta clase implementa el rol de iniciador en un protocolo de interacción Fipa-Contract-Net o Iterated-Fipa-Contract-Net.
import jade.domain.FIPANames;            //Esta clase proporciona un único punto de acceso para el conjunto de constantes ya definidas por FIPA. Las constantes se han agrupado por categoría (es decir, ACLCodecs, lenguajes de contenido, MTP, ...), con una clase interna que implementa cada categoría.

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

@SuppressWarnings("unchecked")

public class Particle_Agent extends Agent {
		private int nResponders;
	
	protected void setup() { 
  	// Leera los nombres de los respondedores como argumentos
  	Object[] args = getArguments();
  	if (args != null && args.length > 0) {
  		nResponders = args.length;
  		System.out.println("PARTICLE: Trying to delegate PSO to one out of "+nResponders+" responders.");
  		
  		// LLenamos el mensaje de CFP
  		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
  		for (int i = 0; i < args.length; ++i) {
  			msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
  		}
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);//MODELO DEL FIPA CONTRACT NET
			// REQUERIMOS UNA RESPUESTA EN 10 SEGUNDOS
			msg.setContent("Perform: Particle Swarm Optimization method");
			
			addBehaviour(new ContractNetInitiator(this, msg) {
				
				protected void handlePropose(ACLMessage propose, Vector v) {
					System.out.println("PARTICLE: Agent "+propose.getSender().getName()+" proposed "+propose.getContent());
				}
				
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println("PARTICLE: Agent "+refuse.getSender().getName()+" refused");
				}
				
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						//MENSAJE DE FALLO POR EL TIEMPO DE EJECUCION DE JADE QUE EL RECEPTOR NO EXISTE, ES DECIR, OTRO AGENTE
						System.out.println("PARTICLE: Responder does not exist");
					}
					else {
						System.out.println("PARTICLE: Agent "+failure.getSender().getName()+" failed");
					}
					nResponders--;
				}
				
				protected void handleAllResponses(Vector responses, Vector acceptances) {
					if (responses.size() < nResponders) {
						// SI ALGUNOS RESPPONDEDORES NO RESPONDIERON DENTRO DEL TIEMPO DE ESPERA ESPECIFICADO
						System.out.println("PARTICLE: Timeout expired: missing "+(nResponders - responses.size())+" responses");
					}
					// EVALUA LOS PROPOSALS
					String bestProposal = "";
					AID bestProposer = null;
					ACLMessage accept = null;

					Enumeration e = responses.elements();
					while (e.hasMoreElements()) {
						ACLMessage msg = (ACLMessage) e.nextElement();
						if (msg.getPerformative() == ACLMessage.PROPOSE) {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.addElement(reply);

								String proposal = String.valueOf(msg.getContent());
								//System.out.println("The proposal is: "+proposal);
							if (proposal !="PSO METHOD") {
								bestProposal = proposal;
								bestProposer = msg.getSender();
								accept = reply;
							}
						}
					}
					// ACEPTAR LA PROPUESTA
					if (accept != null) {
						System.out.println("Particle: Accepting proposal "+bestProposal+" from responder "+bestProposer.getName());
						accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					}						
				}
				//MENSAJE DE INFORMQUE QUE EL AGENTE REALIZACO CON EXITO LA ACCION SOLICITADA
				protected void handleInform(ACLMessage inform) {
					
					System.out.println("PSO: Agent "+inform.getSender().getName()+" successfully performed the requested action");
				}
			} );
  	}
  	else {
  		System.out.println("No responder specified.");
  	}
  } 
}