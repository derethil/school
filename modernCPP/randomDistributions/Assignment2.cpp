#include "distributions.hpp"

// ------------------------------------------------------------------
//
// Testing Code
//
// ------------------------------------------------------------------
#include <functional>
#include <iostream>
#include <numeric>
#include <string>

namespace testing::detail
{
    using namespace std::string_literals;

    using Bins = std::vector<std::pair<std::uint32_t, std::uint32_t>>;
    using DistFunc = std::function<std::vector<DistributionPair>()>;

#define CS3460_ASSERT_EQ(expected, actual, message)                    \
    if (expected != actual)                                            \
    {                                                                  \
        fail(message, "[ Expected", expected, "but got", actual, "]"); \
        return;                                                        \
    }

#define CS3460_CASE(x) \
    [] {               \
        return x;      \
    };                 \
    std::cout << " Case " << #x << "\n";

    template <typename Message>
    void failInternal(const Message& message)
    {
        std::cout << message << " ";
    }

    template <typename Message, typename... Messages>
    void failInternal(const Message& message, const Messages&... messages)
    {
        failInternal(message);
        failInternal(messages...);
    }

    template <typename... Messages>
    void fail(const Messages&... messages)
    {
        std::cout << "  Assertion failed: ";
        failInternal(messages...);
        std::cout << "\n";
    }

    Bins generateBins(const std::uint32_t min, const std::uint32_t max, const std::uint8_t numberBins)
    {
        const auto binRange = (max - min) / numberBins;
        auto minBin = min;
        auto maxBin = min + binRange;

        Bins results(numberBins);
        for (std::uint8_t bin = 0u; bin < numberBins; bin++)
        {
            results[bin] = { minBin, maxBin };
            minBin = maxBin + 1;
            maxBin = minBin + binRange;
        }

        return results;
    }

    void returnsTheExpectedBins(const DistFunc& func, const Bins& bins)
    {
        const auto result = func();
        CS3460_ASSERT_EQ(bins.size(), result.size(), "Wrong number of bins");
        for (auto i = 0u; i < bins.size(); i++)
        {
            CS3460_ASSERT_EQ(bins[i].first, result[i].minValue, "Wrong minimum value for bin "s + std::to_string(i));
            CS3460_ASSERT_EQ(bins[i].second, result[i].maxValue, "Wrong maximum value for bin "s + std::to_string(i));
        }
    }

    void hasTheCorrectTotalAcrossAllBins(const DistFunc& func, const std::uint32_t howMany)
    {
        const auto result = func();
        const auto add_counts = [](std::uint32_t total, const DistributionPair& bin)
        {
            return total + bin.count;
        };
        CS3460_ASSERT_EQ(howMany, std::accumulate(result.cbegin(), result.cend(), 0u, add_counts),
                         "Wrong number of elements across all bins");
    }

    void testUniformDistribution()
    {
        std::cout << "Testing generateUniformDistribution\n";
        auto func = CS3460_CASE(generateUniformDistribution(100000, 0, 79, 40));
        returnsTheExpectedBins(func, generateBins(0, 79, 40));
        hasTheCorrectTotalAcrossAllBins(func, 100000);
    }

    void testNormalDistribution()
    {
        std::cout << "Testing generateNormalDistribution\n";
        auto func = CS3460_CASE(generateNormalDistribution(100000, 50, 5, 40));
        returnsTheExpectedBins(func, generateBins(30, 69, 40));
        hasTheCorrectTotalAcrossAllBins(func, 100000);
    }

    void testPoissonDistribution()
    {
        std::cout << "Testing generatePoissonDistribution\n";
        auto func = CS3460_CASE(generatePoissonDistribution(100000, 6, 40));
        returnsTheExpectedBins(func, generateBins(0, 39, 40));
        hasTheCorrectTotalAcrossAllBins(func, 100000);
    }
} // namespace testing::detail

void test()
{
    using namespace testing::detail;

    testUniformDistribution();
    testNormalDistribution();
    testPoissonDistribution();

    std::cout << "\n\n";
}

int main()
{
    auto uniform = generateUniformDistribution(100000, 0, 79, 40);
    plotDistribution("--- Uniform ---", uniform, 80);

    auto normal = generateNormalDistribution(100000, 50, 5, 40);
    plotDistribution("--- Normal ---", normal, 80);

    auto poisson = generatePoissonDistribution(100000, 6, 40);
    plotDistribution("--- Poisson ---", poisson, 80);

    test();
}