#include "RendererConsole.hpp"

#include "LifeSimulator.hpp"
#include "rlutil.h"

#include <cstdint>
#include <vector>

RendererConsole::RendererConsole() :
    m_sizeX(rlutil::tcols()),
    m_sizeY(rlutil::trows()),
    m_lastState(m_sizeY, std::vector<bool>(m_sizeX, false))
{
    rlutil::cls();
}

void RendererConsole::render(const LifeSimulator& simulation)
{
    rlutil::hidecursor();

    for (std::uint8_t y = 0; y < simulation.getSizeY(); y++)
    {
        for (std::uint8_t x = 0; x < simulation.getSizeX(); x++)
        {
            bool currCell = simulation.getCell(x, y);

            if (m_lastState[y][x] != currCell)
            {
                rlutil::locate(x + 1, y + 1);
                rlutil::setChar(currCell ? 'O' : ' ');
                std::cout.flush();
                m_lastState[y][x] = currCell;
            }
        }
    }

    rlutil::showcursor();
}
