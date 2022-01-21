#pragma once

#include "Pattern.hpp"

#include <array>
#include <cstdint>

class PatternAcorn : public Pattern
{
  public:
    virtual std::uint8_t getSizeX() const override;
    virtual std::uint8_t getSizeY() const override;
    virtual bool getCell(std::uint8_t x, std::uint8_t y) const override;

  private:
    static const std::uint8_t m_sizeX = 7;
    static const std::uint8_t m_sizeY = 3;
    const std::array<std::array<std::uint8_t, m_sizeX>, m_sizeY> m_pattern = { { { 0, 1, 0, 0, 0, 0, 0 },
                                                                                 { 0, 0, 0, 1, 0, 0, 0 },
                                                                                 { 1, 1, 0, 0, 1, 1, 1 } } };
};