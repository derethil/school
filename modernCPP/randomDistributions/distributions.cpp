#include <cstdint>
#include <iomanip>
#include <iostream>
#include <random>
#include <sstream>
#include <vector>

class DistributionPair
{
  public:
    DistributionPair(std::uint32_t minValue, std::uint32_t maxValue) :
        minValue(minValue),
        maxValue(maxValue),
        count(0)
    {
    }

    std::uint32_t minValue;
    std::uint32_t maxValue;
    std::uint32_t count;
};

std::uint32_t getIncrement(std::uint32_t min, std::uint32_t max, std::uint32_t numberBins)
{
    return (max - min) / numberBins + (((max - min) % numberBins) != 0);
}

std::vector<DistributionPair> getBins(std::uint32_t min, std::uint32_t increment, std::uint32_t numberBins)
{
    std::vector<DistributionPair> bins;

    std::uint32_t currMin = min;
    for (std::uint32_t i = 0; i < numberBins; i++)
    {
        bins.push_back(DistributionPair(currMin, currMin + increment - 1));
        currMin += increment;
    }

    return bins;
}

std::uint32_t maxCount(std::vector<DistributionPair> bins)
{
    std::uint32_t max = 0;

    for (DistributionPair bin : bins)
    {
        max = std::max(max, bin.count);
    }

    return max;
}

std::vector<DistributionPair> generateUniformDistribution(std::uint32_t howMany, std::uint32_t min, std::uint32_t max, std::uint8_t numberBins)
{
    int increment = getIncrement(min, max, numberBins);
    std::vector<DistributionPair> bins = getBins(min, increment, numberBins);

    std::random_device rd;
    std::mt19937 engine(rd());

    std::uniform_int_distribution<unsigned int> distInt(min, max);

    for (std::uint32_t i = 1; i <= howMany; i++)
    {
        std::uint32_t num = distInt(engine);
        bins[(num - min) / increment].count++;
    }

    return bins;
}

std::vector<DistributionPair> generateNormalDistribution(std::uint32_t howMany, float mean, float stdev, std::uint8_t numberBins)
{

    float min = mean - 4.f * stdev;
    float max = mean + 4.f * stdev - 1.f;
    int increment = getIncrement(static_cast<int>(min), static_cast<int>(max), numberBins);

    std::vector<DistributionPair> bins = getBins(static_cast<int>(min), static_cast<int>(increment), numberBins);

    std::random_device rd;
    std::mt19937 engine(rd());

    std::normal_distribution<float> distFloat(mean, stdev);

    for (std::uint32_t i = 1; i <= howMany; i++)
    {
        float num = distFloat(engine);

        if (num < min)
        {
            num = min;
        }
        else if (num > max)
        {
            num = max;
        }

        std::uint32_t binIdx = static_cast<int>(num - min) / increment;

        bins[binIdx].count++;
    }

    return bins;
}

std::vector<DistributionPair> generatePoissonDistribution(std::uint32_t howMany, std::uint8_t howOften, std::uint8_t numberBins)
{
    std::uint32_t min = 0;
    std::uint32_t max = numberBins - 1;
    int increment = getIncrement(min, max, numberBins);

    std::vector<DistributionPair> bins = getBins(min, increment, numberBins);

    std::random_device rd;
    std::mt19937 engine(rd());
    std::poisson_distribution<int> distInt(howOften);

    for (std::uint32_t i = 1; i <= howMany; i++)
    {
        std::uint32_t num = distInt(engine);

        if (num > max)
        {
            num = max;
        }

        std::uint32_t binIdx = (num - min) / increment;

        bins[binIdx].count++;
    }

    return bins;
}

void plotDistribution(
    std::string title,
    const std::vector<DistributionPair>& distribution,
    const std::uint8_t maxPlotLineSize)
{
    std::uint32_t starWorth = maxCount(distribution) / maxPlotLineSize;

    std::cout << title << std::endl;

    for (DistributionPair bin : distribution)
    {
        std::stringstream min;
        min << std::setw(2) << bin.minValue;

        std::stringstream max;
        max << std::setw(2) << bin.maxValue;

        std::cout << "[" << min.str() << ", " << max.str() << "] : " << std::string(bin.count / starWorth, '*') << std::endl;
    }

    std::cout << std::endl;
}