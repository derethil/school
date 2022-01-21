#include <string>
#include <iostream>

long nbonacci(unsigned int series, unsigned int n)
{
    if (n < series + 1)
    {
        return 1;
    }
    else
    {
        long total = 0;

        for (unsigned int i = n - series; i < n; i++)
        {
            total += nbonacci(series, i);
        }
        return total;
    }
}

void reportNBonacci(std::string title, unsigned int series, unsigned int max = 20)
{
    std::cout << "--- " << title << " Sequence ---" << std::endl;

    for (unsigned int i = 0; i <= max; i++)
    {
        std::cout << nbonacci(series, i) << " ";
    }

    std::cout << std::endl;
}

void computeNbonacciRatio(std::string title, unsigned int series)
{
    int n = series + 1;
    float prevRatio = 1;
    double difference = 1;

    while (difference > 0.000001)
    {
        double ratio = static_cast<double>(nbonacci(series, n)) / nbonacci(series, n - 1);
        difference = std::abs(ratio - prevRatio);
        prevRatio = ratio;
        n++;
    }

    std::cout << title << " ratio approaches " << prevRatio << " after " << n - 2 << " iterations" << std::endl;
}

int main()
{
    reportNBonacci("Fibonacci", 2);
    reportNBonacci("Tribonacci", 3);
    reportNBonacci("Fourbonacci", 4);
    reportNBonacci("Fivebonacci", 5);

    std::cout << std::endl;

    computeNbonacciRatio("Fibonacci", 2);
    computeNbonacciRatio("Tribonacci", 3);
    computeNbonacciRatio("Fourbonacci", 4);
    computeNbonacciRatio("Fivebonacci", 5);
}
