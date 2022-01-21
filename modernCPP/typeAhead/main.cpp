#include "WordTree.hpp"
#include "rlutil.h"

#include <algorithm>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>

std::shared_ptr<WordTree> readDictionary(std::string filename)
{
    auto wordTree = std::make_shared<WordTree>();
    std::ifstream inFile = std::ifstream(filename, std::ios::in);

    while (!inFile.eof())
    {
        std::string word;
        std::getline(inFile, word);
        // Need to consume the carriage return character for some systems, if it exists
        if (!word.empty() && word[word.size() - 1] == '\r')
        {
            word.erase(word.end() - 1);
        }
        // Keep only if everything is an alphabetic character -- Have to send isalpha an unsigned char or
        // it will throw exception on negative values; e.g., characters with accent marks.
        if (std::all_of(word.begin(), word.end(), [](unsigned char c)
                        { return std::isalpha(c); }))
        {
            std::transform(word.begin(), word.end(), word.begin(), [](char c)
                           { return static_cast<char>(std::tolower(c)); });
            wordTree->add(word);
        }
    }

    return wordTree;
}

void writeString(std::string str, int x, int y)
{
    rlutil::locate(x, y);
    std::cout << str;
}

std::string getInput(std::string totalInput)
{
    rlutil::locate(totalInput.size() + 1, 0);
    auto lastInput = rlutil::getkey();

    if (lastInput == rlutil::KEY_BACKSPACE && totalInput.size() > 0)
    {
        totalInput.pop_back();
    }

    else if (lastInput == rlutil::KEY_BACKSPACE && totalInput.size() == 0)
    {
        return totalInput;
    }

    else
    {
        totalInput += lastInput;
    }

    return totalInput;
}

template <typename Out>
void split(const std::string& s, char delim, Out result)
{
    std::istringstream iss(s);
    std::string item;
    while (std::getline(iss, item, delim))
    {
        *result++ = item;
    }
}

std::vector<std::string> split(const std::string& s, char delim)
{
    std::vector<std::string> elems;
    split(s, delim, std::back_inserter(elems));
    return elems;
}

int main()
{
    auto tree = readDictionary("dictionary.txt");

    rlutil::saveDefaultColor();
    rlutil::cls();

    writeString("--- prediction ---", 0, 3);

    std::string input = "";
    std::uint8_t lastNumPredictions = 0;

    while (true)
    {
        auto lastSize = input.size();
        input = getInput(input);

        writeString(std::string(lastSize, ' '), 0, 0);
        writeString(input, 0, 0);

        auto inputWords = (input.size() > 0) ? split(input, ' ') : std::vector<std::string>{ "" };
        auto numPredictions = rlutil::trows() - 4;

        auto predictions = tree->predict(inputWords.back(), numPredictions);

        for (std::uint8_t i = 0; i < lastNumPredictions; i++)
        {
            writeString(std::string(rlutil::tcols(), ' '), 0, 4 + i);
        }

        for (std::uint8_t i = 0; i < predictions.size(); i++)
        {
            writeString(predictions[i], 0, 4 + i);
        }

        lastNumPredictions = predictions.size();
    }

    rlutil::resetColor();

    return 0;
}