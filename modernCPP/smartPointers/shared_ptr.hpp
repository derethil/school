#pragma once

#include <utility>

namespace usu
{

    // T Definition
    template <typename T>
    class shared_ptr
    {
      public:
        shared_ptr(T* value);
        T* get();
        unsigned int use_count();

        // Structure Overloads
        shared_ptr(const shared_ptr<T>& ptr); // copy constructor
        shared_ptr(shared_ptr<T>&& ptr);      // move constructor
        ~shared_ptr();                        // destructor

        // Operator Overloads
        shared_ptr<T>& operator=(const shared_ptr<T>& rhs); // copy assignment operator
        shared_ptr<T>& operator=(shared_ptr<T>&& rhs);      // move assignment operator
        T operator*();
        T* operator->();

      private:
        unsigned int* m_count = nullptr;
        T* m_raw = nullptr;

        void cleanup();
    };

    // T Operators / Methods

    template <typename T>
    T* shared_ptr<T>::get() { return m_raw; }

    template <typename T>
    unsigned int shared_ptr<T>::use_count() { return *m_count; }

    template <typename T>
    T shared_ptr<T>::operator*() { return *m_raw; }

    template <typename T>
    T* shared_ptr<T>::operator->() { return m_raw; }

    // T Overloaded Constructor
    template <typename T>
    shared_ptr<T>::shared_ptr(T* value) :
        m_count(new unsigned int(1)),
        m_raw(value)
    {
    }

    // T Copy Constructor
    template <typename T>
    shared_ptr<T>::shared_ptr(const shared_ptr<T>& old_ptr)
    {
        m_raw = old_ptr.m_raw;
        m_count = old_ptr.m_count;

        (*m_count)++;
    }

    // T Copy Assignment Operator
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(const shared_ptr<T>& rhs)
    {
        cleanup();

        m_raw = rhs.m_raw;
        m_count = rhs.m_count;

        (*m_count)++;

        return *this;
    }

    // T Move Constructor
    template <typename T>
    shared_ptr<T>::shared_ptr(shared_ptr<T>&& old_ptr)
    {
        m_raw = old_ptr.m_raw;
        m_count = old_ptr.m_count;
        old_ptr.m_raw = nullptr;
        old_ptr.m_count = nullptr;
    }

    // T Move assignment operator
    template <typename T>
    shared_ptr<T>& shared_ptr<T>::operator=(shared_ptr<T>&& rhs)
    {
        cleanup();

        m_raw = rhs.m_raw;
        m_count = rhs.m_count;

        rhs.m_raw = nullptr;
        rhs.m_count = nullptr;

        return *this;
    }

    // T Destructor
    template <typename T>
    shared_ptr<T>::~shared_ptr()
    {
        cleanup();
    }

    template <typename T>
    void shared_ptr<T>::cleanup()
    {
        if (m_count != nullptr && --(*m_count) == 0)
        {
            if (m_raw != nullptr)
            {
                delete m_raw;
            }
            delete m_count;
        }
    }

    // T[] Definition

    template <typename T>
    class shared_ptr<T[]>
    {
      public:
        shared_ptr(T* value, unsigned int numElements);
        unsigned int use_count();
        unsigned int size();

        // Structure Overloads
        shared_ptr(const shared_ptr<T[]>& ptr); // copy constructor
        shared_ptr(shared_ptr<T[]>&& ptr);      // move constructor
        ~shared_ptr();                          // destructor

        // Operator Overloads
        shared_ptr<T[]>& operator=(const shared_ptr<T[]>& rhs); // copy assignment operator
        shared_ptr<T[]>& operator=(shared_ptr<T[]>&& rhs);      // move assignment operator
        T& operator[](int index);

      private:
        unsigned int* m_count = nullptr;
        T* m_raw = nullptr;
        unsigned int m_numElements;

        void cleanup();
    };

    template <typename T>
    shared_ptr<T[]>::shared_ptr(T* pointer, unsigned int numElements) :
        m_raw(pointer), m_numElements(numElements)
    {
    }

    template <typename T>
    unsigned int shared_ptr<T[]>::size()
    {
        return m_numElements;
    }

    template <typename T>
    T& shared_ptr<T[]>::operator[](int index)
    {
        return *(m_raw + index);
    }

    // T Copy Constructor
    template <typename T>
    shared_ptr<T[]>::shared_ptr(const shared_ptr<T[]>& old_ptr)
    {
        m_raw = old_ptr.m_raw;
        m_count = old_ptr.m_count;

        (*m_count)++;
    }

    // T Copy Assignment Operator
    template <typename T>
    shared_ptr<T[]>& shared_ptr<T[]>::operator=(const shared_ptr<T[]>& rhs)
    {
        cleanup();

        m_raw = rhs.m_raw;
        m_count = rhs.m_count;

        (*m_count)++;

        return *this;
    }

    // T Move Constructor
    template <typename T>
    shared_ptr<T[]>::shared_ptr(shared_ptr<T[]>&& old_ptr)
    {
        m_raw = old_ptr.m_raw;
        m_count = old_ptr.m_count;
        old_ptr.m_raw = nullptr;
        old_ptr.m_count = nullptr;
    }

    // T Move assignment operator
    template <typename T>
    shared_ptr<T[]>& shared_ptr<T[]>::operator=(shared_ptr<T[]>&& rhs)
    {
        cleanup();

        m_raw = rhs.m_raw;
        m_count = rhs.m_count;

        rhs.m_raw = nullptr;
        rhs.m_count = nullptr;

        return *this;
    }

    // T Destructor
    template <typename T>
    shared_ptr<T[]>::~shared_ptr()
    {
        cleanup();
    }

    template <typename T>
    void shared_ptr<T[]>::cleanup()
    {
        if (m_count != nullptr && --(*m_count) == 0)
        {
            if (m_raw != nullptr)
            {
                for (unsigned int i = 0; i < m_numElements; i++)
                {
                    delete[] m_raw;
                }
            }
            delete m_count;
        }
    }

    // Make Shared

    template <typename T, typename... Args>
    shared_ptr<T> make_shared(Args&&... args)
    {
        return shared_ptr<T>(new T(std::forward<Args>(args)...));
    }

    template <typename T, unsigned int N>
    shared_ptr<T[]> make_shared_array()
    {
        return shared_ptr<T[]>(new T[N], N);
    }

} // namespace usu
