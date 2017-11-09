package Evolutionary;

import org.junit.Test;

import static org.junit.Assert.*;


public class DomainTest {
    Domain d = new Domain();
    
    
    @Test
    public void testComputeFitness() {
    	d.initializeDomain(8,10000,2,5,0.002,0.001);
        Individual i = new Individual("01010101");
        assertEquals(String.valueOf(d.computeFitness(i)), "0.5");

    }

    
	@Test
	public void testInitializeDomain() {
		d.initializeDomain(8,10000,2,5,0.002,0.001);
	}

	@Test
	public void testGetBitLength() {
		// System.out.println(d.getGenNum());
    	assertEquals(d.getBitLength(),8);
	}

	@Test
	public void testGetPopSize() {
		assertEquals(d.getPopSize(),10000);
	}

	@Test
	public void testGetCrossNum() {
    	System.out.print(d.getCrossNum());
		assertEquals(d.getCrossNum(),2);
	}

	@Test
	public void testGetGenNum() {
		assertEquals(d.getGenNum(),5);
	}

	@Test
	public void testGetSurRatio() {
		assertEquals(String.valueOf(d.getSurRatio()),"0.002");
	}

	@Test
	public void testGetMutationRate() {
		assertEquals(String.valueOf(d.getMutationRate()),"0.001");
	}


}