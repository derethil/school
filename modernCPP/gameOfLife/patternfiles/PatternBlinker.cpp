#include "PatternBlinker.hpp"

#include <cstdint>

std::uint8_t PatternBlinker::getSizeX() const
{
    return PatternBlinker::m_sizeX;
}
std::uint8_t PatternBlinker::getSizeY() const
{
    return PatternBlinker::m_sizeY;
}

bool PatternBlinker::getCell(std::uint8_t x, std::uint8_t y) const
{
    return PatternBlinker::m_pattern[y][x];
}