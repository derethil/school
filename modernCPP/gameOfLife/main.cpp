

#include "LifeSimulator.hpp"
#include "RendererConsole.hpp"
#include "patternfiles/PatternAcorn.hpp"
#include "patternfiles/PatternBlinker.hpp"
#include "patternfiles/PatternBlock.hpp"
#include "patternfiles/PatternGlider.hpp"
#include "patternfiles/PatternGosperGliderGun.hpp"
#include "rlutil.h"

#include <chrono>
#include <thread>
#include <vector>

int main()
{
    auto renderer = RendererConsole();
    auto simulator = LifeSimulator(rlutil::tcols(), rlutil::trows());

    PatternBlock block;
    PatternGlider glider;
    PatternAcorn acorn;
    PatternGosperGliderGun gliderGun;
    PatternBlinker blinker;

    simulator.insertPattern(gliderGun, 0, 0);

    for (int steps = 0; steps < 300; steps++)
    {
        renderer.render(simulator);
        simulator.update();
        std::this_thread::sleep_for(std::chrono::milliseconds(10));
    }
}