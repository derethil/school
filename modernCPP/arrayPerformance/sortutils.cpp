#include "sortutils.hpp"

#include <algorithm>
#include <chrono>
#include <execution>
#include <iomanip>
#include <iostream>

enum class ArrayType : int
{
    RAW,
    ARRAY,
    VECTOR
};

enum class Policy : int
{
    SEQ,
    PAR
};

void printTime(std::string title, std::chrono::nanoseconds time)
{
    std::cout << "    " << std::left << std::setw(16) << title + " Time"
              << " : " << std::chrono::duration_cast<std::chrono::milliseconds>(time).count() << " ms " << std::endl;
}

void initializeRawArrayFromStdArray(const SourceArray& source, int dest[])
{
    for (std::size_t i = 0; i < source.size(); i++)
    {
        dest[i] = source[i];
    }
}

void organPipeStdArray(SourceArray& data)
{
    for (std::size_t close = 0ul, far = data.size() - 1; close < far; close++, far--)
    {
        data[far] = data[close];
    }
}

void evaluateSort(const SourceArray& data, std::string title, ArrayType arrayType, Policy policy)
{
    using namespace std::chrono;

    auto totalDuration = nanoseconds(0);

    for (int i = 0; i < HOW_MANY_TIMES; i++)
    {
        if (arrayType == ArrayType::RAW)
        {
            int rawArray[HOW_MANY_ELEMENTS];
            initializeRawArrayFromStdArray(data, rawArray);
            auto start = steady_clock::now();

            if (policy == Policy::PAR)
            {
                std::sort(std::execution::par, rawArray, rawArray + HOW_MANY_ELEMENTS);
            }
            else
            {
                std::sort(rawArray, rawArray + HOW_MANY_ELEMENTS);
            }

            auto end = steady_clock::now();
            totalDuration += end - start;
        }
        else if (arrayType == ArrayType::ARRAY)
        {
            std::array array = data;
            auto start = steady_clock::now();

            if (policy == Policy::PAR)
            {
                std::sort(std::execution::par, array.begin(), array.end());
            }
            else
            {
                std::sort(array.begin(), array.end());
            }

            auto end = steady_clock::now();
            totalDuration += end - start;
        }
        else
        {
            std::vector<int> vector(data.begin(), data.end());
            auto start = steady_clock::now();

            if (policy == Policy::PAR)
            {
                std::sort(std::execution::par, vector.begin(), vector.end());
            }
            else
            {
                std::sort(vector.begin(), vector.end());
            }

            auto end = steady_clock::now();
            totalDuration += end - start;
        }
    }

    printTime(title, totalDuration);
}

void evaluatePrep(
    const SourceArray& random,
    const SourceArray& sorted,
    const SourceArray& reversed,
    const SourceArray& organPipe,
    const SourceArray& rotated,
    ArrayType arrayType,
    std::string title)
{
    std::cout << " --- " << title << " Performance ---" << std::endl;
    std::cout << std::endl;

    std::cout << "Sequential" << std::endl;

    evaluateSort(random, "Random", arrayType, Policy::SEQ);
    evaluateSort(sorted, "Sorted", arrayType, Policy::SEQ);
    evaluateSort(reversed, "Reversed", arrayType, Policy::SEQ);
    evaluateSort(organPipe, "Organ Pipe", arrayType, Policy::SEQ);
    evaluateSort(rotated, "Rotated", arrayType, Policy::SEQ);

    std::cout << std::endl;
    std::cout << "Parallel" << std::endl;

    evaluateSort(random, "Random", arrayType, Policy::PAR);
    evaluateSort(sorted, "Sorted", arrayType, Policy::PAR);
    evaluateSort(reversed, "Reversed", arrayType, Policy::PAR);
    evaluateSort(organPipe, "Organ Pipe", arrayType, Policy::PAR);
    evaluateSort(rotated, "Rotated", arrayType, Policy::PAR);

    std::cout << std::endl;
}

void evaluateRawArray(
    const SourceArray& random,
    const SourceArray& sorted,
    const SourceArray& reversed,
    const SourceArray& organPipe,
    const SourceArray& rotated)
{
    evaluatePrep(
        random,
        sorted,
        reversed,
        organPipe,
        rotated,
        ArrayType::RAW,
        "Raw Array");
}

void evaluateStdArray(
    const SourceArray& random,
    const SourceArray& sorted,
    const SourceArray& reversed,
    const SourceArray& organPipe,
    const SourceArray& rotated)
{
    evaluatePrep(
        random,
        sorted,
        reversed,
        organPipe,
        rotated,
        ArrayType::ARRAY,
        "std::array");
}

void evaluateStdVector(
    const SourceArray& random,
    const SourceArray& sorted,
    const SourceArray& reversed,
    const SourceArray& organPipe,
    const SourceArray& rotated)
{
    evaluatePrep(
        random,
        sorted,
        reversed,
        organPipe,
        rotated,
        ArrayType::VECTOR,
        "std::vector");
}
