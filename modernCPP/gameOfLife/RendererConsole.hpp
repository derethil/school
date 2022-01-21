#pragma once

#include "Renderer.hpp"

#include <cstdint>
#include <vector>

class RendererConsole : public Renderer
{
  public:
    RendererConsole();

    virtual void render(const LifeSimulator& simulation);

  private:
    const std::uint8_t m_sizeX;
    const std::uint8_t m_sizeY;
    std::vector<std::vector<bool>> m_lastState;
};