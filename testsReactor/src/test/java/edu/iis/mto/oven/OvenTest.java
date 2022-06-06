package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    Oven oven;
    BakingProgram bakingProgram;
    BakingResult bakingResult;
    List<ProgramStage> stagesList;

    @Mock
    HeatingModule heatingModule;

    @Mock
    Fan fan;

    @BeforeEach
    void setUp() {
        oven = new Oven(heatingModule, fan);
        stagesList = new ArrayList<>();
        bakingProgram =
                BakingProgram.builder()
                        .withCoolAtFinish(false)
                        .withInitialTemp(0)
                        .withStages(stagesList)
                        .build();
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
