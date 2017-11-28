package Evolutionary;

import java.text.DecimalFormat;
import java.util.*;
import java.lang.Math;
import java.util.Random;

public class Main {
    private Random rand = new Random();


    // Make an init method that instantiates a domain object
    
    /**
     * The method whoLives calculates the survivors based on a tournament selection algorithm
     * @param population - an array of individuals, representing the population
     * @param domain
     * @return Array<Individual> - the individuals that have been chosen to survive
     */
    public ArrayList<Individual> whoLives(ArrayList<Individual> population, Domain domain){
        ArrayList<Individual> tempList = new ArrayList<>();
        ArrayList<Individual> tempPop = new ArrayList<>(population); // changeable list of the population
        while(tempList.size() < Math.floor(domain.getSurRatio() * population.size())) {

            // Randomly select participants for the tournament
            ArrayList<Individual> participants = selectParticipants(tempPop, domain);

            // Select Winner
            Individual winner = selectWinner(participants);

            //Add winner to list of winners
            tempList.add(winner);

            // Remove the winner from the population list
            tempPop.remove(winner);

        }
        return tempList;
    }
    
    
    /**
     * selectParticipants randomly chooses participants from a population to compete in a tournament
     * @param population the population from which the participants are being chosen
     * @return ArrayList<Individual> the participants selected for the tournament
     */
    public ArrayList<Individual> selectParticipants(ArrayList<Individual> population,  Domain domain){
        ArrayList<Individual> tParticipants = new ArrayList<>();
        for(int i = 0 ; i < domain.getTSize() ; i++){
            int y = rand.nextInt(population.size());
            tParticipants.add(population.get(y));
        }
        return tParticipants;
    }
    
    /**
     * selectWinner chooses a winner based on highest fitness out of a tournament of Individuals
     * @param participants <Individual> the participants in the tournament
     * @return Individual - the winner of the tournament
     */
    public Individual selectWinner(ArrayList<Individual> participants){
        Individual winner = participants.get(0);
        double winnerFitness = winner.getFitness();
        for(int i = 1 ; i < participants.size() ; i++){
            double nextFitness = participants.get(i).getFitness();
            if(nextFitness > winnerFitness){
                winner = participants.get(i);
                winnerFitness = nextFitness;
            }
        }
        return winner;
    }
    
    /**
     * The method createInitPop creates an ArrayList<Individual> that represents the population. These individuals
     * are created randomly.
     * @param popSize - the population size, as set in the Domain.java class
     * @return Array<Individual> - the initial population representing the first generation of the test
     */
    public ArrayList<Individual>  createInitPop(int popSize, Domain domain){
        ArrayList<Individual> population = new ArrayList<>();
        for (int i = 0 ; i < popSize ; i++) {
            population.add(new Individual(domain));
        }
        return population;
    }
    
    /**
     * This method will take a population as an ArrayList<Individual> and will return the new population after mutations
     * @param population - an ArrayList<Individual> representing the entire population
     * @return ArrayList<Individual> - the new population after mutations have occurred
     */
    public ArrayList<Individual> mutate(ArrayList<Individual> population, Domain domain){
        for (int i = 0; i < population.size() - 1 ; i++){
            double y = rand.nextDouble();
            if (y <= domain.getMutationRate()) {
                population.get(i).flipBit(domain);
            }
        }
        return population;
    }

    /**
     * This method is going to create a list of all the indexes of the spliets.
     * @param father: first parent
     * @param mother: second parent
     * @return an ArrayList that has two new children.
     */
    public ArrayList<Individual> reproduce(Individual father, Individual mother, Domain domain){
         ArrayList<Integer> allSplits = gitSplits(domain);
         ArrayList<Individual> kids = sliceAndDice(domain, allSplits, father.getGenMak(), mother.getGenMak());
        return kids;
    }

    public ArrayList<Integer> gitSplits(Domain domain){
        ArrayList<Integer> splitsIndexes = new ArrayList<>(); // all the splits indexes.
        int splitNum = domain.getCrossNum();
        while (splitNum != 0){
            // generate a number from 1 to len of the father or the mother - 1
            int randomSplit = rand.nextInt( domain.getBitLength() - 1) + 1;
            if (!splitsIndexes.contains(randomSplit)){
                splitsIndexes.add(randomSplit);
                splitNum--;
            }
        }
        splitsIndexes.add(0, 0);
        splitsIndexes.add(domain.getBitLength());
        Collections.sort(splitsIndexes);
        return splitsIndexes;
    }

    /**
     * This method is going to create the a kid basted on the input.
     * @param allIndexes : where all the slplits will take place.
     * @param father : father indeviual object
     * @param mother : mother indeviual object
     * @return
     */
     ArrayList<Individual> sliceAndDice(Domain domain, ArrayList<Integer> allIndexes, String father, String mother){
        String kid1 = "";
        String kid2 = "";
         int sub = 0;
         while (allIndexes.size()  - 1 >= sub){
             if (father.length() != kid1.length() && mother.length() != kid2.length()) {
                 kid1 += father.substring(allIndexes.get(sub), allIndexes.get(sub + 1));
                 kid2 += mother.substring(allIndexes.get(sub), allIndexes.get(sub + 1));
             }

             if (father.length() != kid1.length() && mother.length() != kid2.length()){

                 kid1 += mother.substring( allIndexes.get(sub + 1), allIndexes.get(sub + 2));
                 kid2 += father.substring( allIndexes.get(sub + 1), allIndexes.get(sub + 2));
             }
             sub+=2;
         }
        return twoKids(domain, kid1, kid2);
    }
    /**
     * @param domain
     * @param firstKid
     * @param secondKid
     * @return @return an ArrayList of two new born kids.
     */
    private ArrayList<Individual> twoKids(Domain domain, String firstKid, String secondKid) {
        ArrayList<Individual> newKids = new ArrayList<>();
        newKids.add(new Individual(domain, firstKid));
        newKids.add(new Individual(domain, secondKid));
        return newKids;
    }

    /**
     * This method takes a population and returns the average fitness
     * @param pop the population; an ArrayList of Individuals
     * @return the average fitness as a double
     */
    public double avgFitness(ArrayList<Individual> pop) {
        double sum = 0;
        for(int i = 0; i < pop.size(); i++) {
            sum += pop.get(i).getFitness();
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(sum / pop.size()));
    }

    /**
     * This method returns the max fitness in the population
     * @param pop the population; an ArrayList of Individuals
     * @return the max fithess as a double
     */
    public double maxFitness(ArrayList<Individual> pop) {
        Individual maxfit = Collections.max(pop, new IndividualComp());
        return maxfit.getFitness();
    }

    /**
     * This method returns the min fitness in the population
     * @param pop the population; an ArrayList of Individuals
     * @return the min fithess as a double
     */
    public double minFitness(ArrayList<Individual> pop) {
        Individual minfit = Collections.min(pop, new IndividualComp());
        return minfit.getFitness();
    }
    public void runGen(Domain domain){

        ArrayList<Individual> initPop = createInitPop(domain.getPopSize(), domain); // todo: this shouldn't be here.
        ArrayList<Individual> kids = new ArrayList<>();
        ArrayList<Individual> adults;
        int gen = domain.getGenNum();
        while (gen != 0) {
            adults = whoLives(initPop, domain);
            int aSize = adults.size();

            while (aSize < domain.getPopSize()) {
                int p1 = rand.nextInt((initPop.size())); // chose random father.
                int p2 = rand.nextInt((initPop.size()));// chose random mother.

                  kids.addAll(reproduce(initPop.get(p1), initPop.get(p2), domain));
                  aSize += 2;
            }
            // make sure that it is even.
            if (kids.size() - adults.size() != domain.getPopSize()) {
                kids.remove((kids.size() - 1)); // remove the last kid.
            }

            initPop = mutate(newGen, domain);
            genData(gen, initPop);
            gen--;
        }

    }

    private void genData(int gen, ArrayList<Individual> initPop) {
        // print average fitness , max fitness , worst fitness
        System.out.println("This is the data for generation num: "+gen);
        System.out.println("This is the avgFitness "+avgFitness(initPop));
        System.out.println("This is the maxFitness "+maxFitness(initPop));
        System.out.println("This is the minFitness "+minFitness(initPop));
    }

    public static void main(String[] args) throws Exception {
        Domain domain = new Domain();
        Main main = new Main();
        // The greater tha bitLength the more interesting the results are.
        domain.initializeDomain(100,10000,5,15,20,
                0.8,0.9);
        main.runGen(domain);
    }
}
