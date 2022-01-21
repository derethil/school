#include <chrono>
#include <cstdint>
#include <iostream>
#include <ratio>

namespace usu
{
    // Class Definition

    template <typename R, typename T = std::uint64_t>
    class weight
    {
      public:
        weight();
        weight(T count);
        T count() const;

        using conversion = R;

      private:
        T m_count;
    };

    // Class Constructors

    template <typename R, typename T>
    weight<R, T>::weight() :
        m_count(0)
    {
    }

    template <typename R, typename T>
    weight<R, T>::weight(T count) :
        m_count(count)
    {
    }

    // Class Methods

    template <typename R, typename T>
    T weight<R, T>::count() const
    {
        return m_count;
    }

    // Arithmetic Operators

    template <typename R, typename T, typename RRight, typename TRight>
    weight<R, T> operator+(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return weight<R, T>(left.count() + weight_cast<weight<R, T>>(right).count());
    }

    template <typename R, typename T, typename RRight, typename TRight>
    weight<R, T> operator-(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return weight<R, T>(left.count() - weight_cast<weight<R, T>>(right).count());
    }

    template <typename R, typename T>
    T operator*(const double left, const weight<R, T>& right)
    {
        return left * right.count();
    }

    template <typename R, typename T>
    T operator*(const weight<R, T>& left, const double right)
    {
        return left.count() * right;
    }

    // Logical Operators

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator==(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() == weight_cast<weight<R, T>>(right).count();
    }

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator!=(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() != weight_cast<weight<R, T>>(right).count();
    }

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator<(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() < weight_cast<weight<R, T>>(right).count();
    }

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator>(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() > weight_cast<weight<R, T>>(right).count();
    }

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator<=(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() <= weight_cast<weight<R, T>>(right).count();
    }

    template <typename R, typename T, typename RRight, typename TRight>
    bool operator>=(const weight<R, T>& left, const weight<RRight, TRight>& right)
    {
        return left.count() >= weight_cast<weight<R, T>>(right).count();
    }

    // Other

    template <typename NewWeight, typename R, typename T>
    NewWeight weight_cast(const weight<R, T>& oldWeight)
    {
        double conversion = R::num * NewWeight::conversion::den;

        conversion /= (R::den * NewWeight::conversion::num);

        return NewWeight(oldWeight.count() * conversion);
    }

    using microgram = weight<std::ratio<1, 1000000>>;
    using gram = weight<std::ratio<1, 1>>;
    using kilogram = weight<std::ratio<1000, 1>>;
    using ounce = weight<std::ratio<28349523125, 1000000000>, double>;
    using pound = weight<std::ratio<45359237, 100000>, double>;
    using ton = weight<std::ratio<90718474, 100>, double>;
}; // namespace usu