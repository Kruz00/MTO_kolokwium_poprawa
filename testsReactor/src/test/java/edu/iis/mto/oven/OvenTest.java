package edu.iis.mto.oven;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldCompleteEmptyStage() {
        stagesList.add(
                ProgramStage.builder()
                        .withStageTime(0)
                        .withTargetTemp(0)
                        .withHeat(HeatType.THERMO_CIRCULATION)
                        .build()
        );
        bakingResult = oven.runProgram(bakingProgram);
        assertTrue(bakingResult.isSuccess());
        assertEquals(1, bakingResult.getStageCompletenes().size());
        bakingResult.getStageCompletenes()
                .forEach((stage, completeness) -> assertTrue(completeness));
    }

    @Test
    void shouldNotCompleteStage_HeatingModuleError() throws HeatingException {
        stagesList.add(
                ProgramStage.builder()
                        .withStageTime(0)
                        .withTargetTemp(0)
                        .withHeat(HeatType.THERMO_CIRCULATION)
                        .build()
        );
        doThrow(HeatingException.class).when(heatingModule).termalCircuit(any());

        bakingResult = oven.runProgram(bakingProgram);
        assertFalse(bakingResult.isSuccess());
        assertEquals(1, bakingResult.getStageCompletenes().size());
        bakingResult.getStageCompletenes()
                .forEach((stage, completeness) -> assertFalse(completeness));
    }

    @Test
    void shouldNotSuccess_FanAlwaysOff(){
        stagesList.add(
                ProgramStage.builder()
                        .withStageTime(0)
                        .withTargetTemp(0)
                        .withHeat(HeatType.THERMO_CIRCULATION)
                        .build()
        );
        when(fan.isOn()).thenReturn(false);

        bakingProgram =
                BakingProgram.builder()
                        .withCoolAtFinish(true)
                        .withInitialTemp(0)
                        .withStages(stagesList)
                        .build();

        bakingResult = oven.runProgram(bakingProgram);
        assertFalse(bakingResult.isSuccess());
    }

    @Test
    void shouldNotCompleteStage_HeatingModuleHeaterError() throws HeatingException {
        stagesList.add(
                ProgramStage.builder()
                        .withStageTime(10)
                        .withTargetTemp(200)
                        .withHeat(HeatType.HEATER)
                        .build()
        );
        doThrow(HeatingException.class).when(heatingModule).heater(any());

        bakingResult = oven.runProgram(bakingProgram);
        assertFalse(bakingResult.isSuccess());
        assertEquals(1, bakingResult.getStageCompletenes().size());
        bakingResult.getStageCompletenes()
                .forEach((stage, completeness) -> assertFalse(completeness));
    }


}
