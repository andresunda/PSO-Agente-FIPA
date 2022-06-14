/**
 * 
 *
 */
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import java.text.DecimalFormat;


@SuppressWarnings("unchecked")
public class PSO_Agent extends Agent {
	 
	
	protected void setup() {
		System.out.println("PSO: Agent "+getLocalName()+" waiting for CFP!");//LLAMANDO A LA PROPUESTA (PROPOSAL)
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			@Override

			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				System.out.println("PSO: Agent "+getLocalName()+": CFP received from "+cfp.getSender().getName()+". Action is "+cfp.getContent());
				


				String proposal = evaluateAction();
					//REQUERIMOS LA PROPUESTA
					System.out.println("PSO: Agent "+getLocalName()+": Proposing "+proposal);
					ACLMessage propose = cfp.createReply();
					propose.setPerformative(ACLMessage.PROPOSE);
					propose.setContent(String.valueOf(proposal));
					return propose;
			}

			@Override
			//ACEPTACION DE PROPUESTA DEL AGENTE Y ASI COMENZAR EL ALGORITMO DEL PSO
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				System.out.println("PSO: Agent "+getLocalName()+": Proposal accepted!");
				System.out.println("Calculanding iterations...");
					
                                    performAction();

					System.out.println("PSO: Agent "+getLocalName()+": Action successfully performed!");
					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					//inform.setContent(String.valueOf(route));
					return inform;
					
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("PSO: Agent "+getLocalName()+": Proposal rejected!!!");
			}
		} );
	}

	private String evaluateAction() {
		// Simulate an evaluation by generating a random number
		String str = "PSO METHOD";
		return str;
	}

	private void performAction() {
        DecimalFormat df = new DecimalFormat("#.###");
        Methods m = new Methods();
        DataSet ds = new DataSet();
        int t=0;

        do{
            if(t==0){
                //PASO 1
                    ds.setX_values(m.initPopulation());
                    ds.setVelocidad(m.initVelocity());
                    ds.setX_values(m.initPosition( ds.getX_values(), ds.getVelocidad()) );
                    ds.setpBest(m.initPBest(ds.getX_values()));
                //PASO 2
                    ds.setFitnessFuntion(m.calculateFitnessFuntion(ds.getX_values()));
                    ds.setMinFitnessValue(m.fitnessValue(ds.getFitnessFuntion()));
                    ds.setgBest(m.calculateGlobalBest(ds.getFitnessFuntion(), ds.getMinFitnessValue(),ds.getX_values()));
            }else{
                //PASO 3
                    ds.setVelocidad(m.updateVelocity(ds.getVelocidad(), ds.getX_values(), ds.getpBest(), ds.getgBest()));
                    System.out.println("Matriz Velocidad:");
                    m.showMatrix(ds.getVelocidad());
                    ds.setX_values(m.updatePosition(ds.getVelocidad(), ds.getX_values()));
                    System.out.println("Matriz Proposicion:");
                    m.showMatrix(ds.getX_values());
                    //PASO 4
                    ds.setMinFitnessValue(m.updateFitnessValue(ds.getMinFitnessValue(), m.calculateFitnessFuntion(ds.getX_values())));
                    ds.setgBest(m.calculateGlobalBest(m.calculateFitnessFuntion(ds.getX_values()), ds.getMinFitnessValue(), ds.getX_values())); 
                    ds.setpBest(m.updatePBest(ds.getX_values(), ds.getpBest(), ds.getFitnessFuntion(), m.calculateFitnessFuntion(ds.getX_values())));
            }
                //PASO 5
                t++;
                //PASO 6
                System.out.println("\nBest particle: "); m.showArray(ds.getgBest());
                System.out.println("Global best: "+df.format(ds.getMinFitnessValue()));
        }while(t<m.iterations);
	}
}