
#include "PatternAcorn.hpp"

#include <cstdint>

std::uint8_t PatternAcorn::getSizeX() const
{
    return PatternAcorn::m_sizeX;
}
std::uint8_t PatternAcorn::getSizeY() const
{
    return PatternAcorn::m_sizeY;
}

bool PatternAcorn::getCell(std::uint8_t x, std::uint8_t y) const
{
    return PatternAcorn::m_pattern[y][x];
}