package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    Oven oven;
    BakingProgram bakingProgram;
    BakingResult bakingResult;

    @Mock
    HeatingModule heatingModule;

    @Mock
    Fan fan;

    @BeforeEach
    void setUp() {
        oven = new Oven(heatingModule, fan);
        bakingProgram = BakingProgram.builder().withCoolAtFinish(false).withInitialTemp(0).build();
    }

    @Test
    void itCompiles() {
        assertThat(true, equalTo(true));
    }

    @Test
    void shouldWorks() {
        bakingResult = oven.runProgram(bakingProgram);
        assertTrue(bakingResult.isSuccess());
    }

}
