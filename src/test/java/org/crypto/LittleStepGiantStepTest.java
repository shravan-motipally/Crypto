package org.crypto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class LittleStepGiantStepTest {


    @Test
    public void testLSGS() throws Exception {
//        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 3, 29);
        LittleStepGiantStep.findDiscreteLog_B_Of_A_InZ_P(2, 13, 29);
    }
}