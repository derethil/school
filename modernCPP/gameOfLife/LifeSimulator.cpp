#include "LifeSimulator.hpp"

#include "rlutil.h"

#include <algorithm>
#include <cstdint>
#include <cstring>
#include <vector>

LifeSimulator::LifeSimulator(std::uint8_t sizeX, std::uint8_t sizeY) :
    m_sizeX(sizeX),
    m_sizeY(sizeY)
{
    for (std::size_t i = 0; i < m_sizeY; ++i)
    {
        m_simulation.push_back(std::vector<bool>(m_sizeX, 0));
    }
}

std::uint8_t LifeSimulator::getSizeX() const
{
    return m_sizeX;
};
std::uint8_t LifeSimulator::getSizeY() const
{
    return m_sizeY;
};
bool LifeSimulator::getCell(std::uint8_t x, std::uint8_t y) const
{
    return m_simulation[y][x];
};

void LifeSimulator::insertPattern(const Pattern& pattern, std::uint8_t startX, std::uint8_t startY)
{
    for (std::uint8_t y = 0; y < pattern.getSizeY(); y++)
    {
        for (std::uint8_t x = 0; x < pattern.getSizeX(); x++)
        {
            m_simulation[y + startY][x + startX] = pattern.getCell(x, y);
        }
    }
}

std::uint8_t LifeSimulator::countNeighbors(std::uint8_t cellX, std::uint8_t cellY) const
{
    std::uint8_t count = 0;

    for (std::int8_t x = cellX - 1; x <= cellX + 1; x++)
    {
        for (std::int8_t y = cellY - 1; y <= cellY + 1; y++)
        {
            if (x < 0 || x > m_sizeX - 1 || y < 0 || y > m_sizeY - 1 || (y == cellY && x == cellX))
            {
                continue;
            }

            if (m_simulation[y][x])
            {
                count++;
            }
        }
    }

    return count;
}

void LifeSimulator::update()
{
    auto newSimulation = m_simulation;
    for (std::size_t i = 0; i < m_sizeY; i++)
    {
        for (std::size_t j = 0; j < m_sizeX; j++)
        {
            std::uint8_t alive = countNeighbors(j, i);

            if (alive < 2)
            {
                newSimulation[i][j] = false;
            }
            else if (alive == 3)
            {
                newSimulation[i][j] = true;
            }
            else if (alive > 3)
            {
                newSimulation[i][j] = false;
            }
        }
    }
    m_simulation = newSimulation;
}