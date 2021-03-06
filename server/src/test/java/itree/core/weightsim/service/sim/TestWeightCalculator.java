package itree.core.weightsim.service.sim;


import itree.core.weightsim.jpa.entity.WeightConfig;
import itree.core.weightsim.model.SimConfig;
import itree.core.weightsim.service.sim.WeightCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestWeightCalculator
{
    @Mock
    private SimConfig simConfig;

    private WeightCalculator weightCalculator;

    private WeightConfig weightConfig;

    private static final String CODE = "BUS";

    @Before
    public void setup()
    {
        weightConfig = new WeightConfig(CODE, "Testing", 20.0, 2, 2, new Long[]{10L, 10L}, new Boolean[]{true, true}, new Boolean[]{false});
        weightCalculator = new WeightCalculator(simConfig);
        when(simConfig.getOverweight()).thenReturn(new Boolean[]{false, false});
    }

    @Test
    public void testOverweight()
    {
        reset(simConfig);
        when(simConfig.getOverweight()).thenReturn(new Boolean[]{true, false});
        List<Double> weights = weightCalculator.getWeight(weightConfig);
        assertEquals(weights.get(0), 11.0);
        assertEquals(weights.get(1), 10.0);
    }

    @Test
    public void testJoin()
    {
        weightConfig.setScaleJoin(new Boolean[]{true});
        List<Double> weights = weightCalculator.getWeight(weightConfig);
        assertEquals(weights.get(0), 5.0);
        assertEquals(weights.get(1), 5.0);
    }

    @Test
    public void testNotActive()
    {
        weightConfig.setScaleActive(new Boolean[]{true, false});
        List<Double> weights = weightCalculator.getWeight(weightConfig);
        assertEquals(weights.get(0), 10.0);
        assertEquals(weights.get(1), 9.99);
    }

    @Test
    public void testComplex()
    {
        weightConfig.setScaleActive(new Boolean[]{true, false});
        weightConfig.setScaleJoin(new Boolean[]{true});
        reset(simConfig);
        when(simConfig.getOverweight()).thenReturn(new Boolean[]{true, true});
        List<Double> weights = weightCalculator.getWeight(weightConfig);
        assertTrue(Double.compare(weights.get(0), 5.5) == 0);
        assertTrue(Double.compare(weights.get(1), 10.989) == 0);
    }

}
