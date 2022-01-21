#include "PatternGosperGliderGun.hpp"

#include <cstdint>

std::uint8_t PatternGosperGliderGun::getSizeX() const
{
    return PatternGosperGliderGun::m_sizeX;
}
std::uint8_t PatternGosperGliderGun::getSizeY() const
{
    return PatternGosperGliderGun::m_sizeY;
}

bool PatternGosperGliderGun::getCell(std::uint8_t x, std::uint8_t y) const
{
    return PatternGosperGliderGun::m_pattern[y][x];
}