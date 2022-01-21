#include "sortutils.hpp"

#include <algorithm>
#include <array>
#include <chrono>
#include <execution>
#include <iostream>
#include <random>

SourceArray generateRandomArray()
{
    const int MIN_VALUE = -10000000;
    const int MAX_VALUE = 10000000;

    std::default_random_engine engine{ 0u };
    std::uniform_int_distribution<> dist{ MIN_VALUE, MAX_VALUE };

    SourceArray array;
    std::generate(array.begin(), array.end(), [&]
                  { return dist(engine); });
    return array;
}

int main()
{
    SourceArray array = generateRandomArray();

    SourceArray sorted = array;
    std::sort(sorted.begin(), sorted.end());

    SourceArray organPipe = sorted;
    organPipeStdArray(organPipe);

    SourceArray reversed = sorted;
    std::reverse(reversed.begin(), reversed.end());

    SourceArray rotated = sorted;
    std::rotate(rotated.begin(), rotated.begin() + 1, rotated.end());

    evaluateRawArray(array, sorted, reversed, organPipe, rotated);
    evaluateStdArray(array, sorted, reversed, organPipe, rotated);
    evaluateStdVector(array, sorted, reversed, organPipe, rotated);
}
