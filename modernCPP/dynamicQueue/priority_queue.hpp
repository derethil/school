#pragma once

#include <cstdint>
#include <initializer_list>
#include <typeinfo>
#include <utility>
#include <vector>

namespace usu
{
    // Class Definition
    template <typename T, typename R = unsigned int>
    class priority_queue
    {
      public:
        class item
        {
          public:
            T value;
            R priority;
        };

      private:
        // Type aliases
        using size_type = std::size_t;
        using value_type = T;
        using priority_type = R;

        // Private data
        std::vector<item> m_heap;
        size_type m_max_size;
        size_type m_num_items;

        // Private methods
        bool isLeaf(size_type position);
        size_type leftChild(size_type position);
        size_type rightChild(size_type position);
        size_type parent(size_type position);
        void siftdown(size_type position);

      public:
        // Item class

        // Constructors
        priority_queue();
        priority_queue(std::initializer_list<std::pair<value_type, priority_type>> list);

        // Priority queue methods
        void enqueue(value_type value, priority_type priority);
        item dequeue();
        bool empty();
        size_type size();
        void buildheap();

        // Iterator class
        class iterator : public std::iterator<std::forward_iterator_tag, priority_queue*>
        {
          public:
            using iterator_category = std::forward_iterator_tag;

            iterator() :
                iterator(nullptr) {} // default constructor

            iterator(std::vector<item>* ptr) :
                m_pos(0), m_data(ptr) {}

            iterator(size_type pos, std::vector<item>* ptr) :
                m_pos(pos), m_data(ptr) {}

            iterator(const iterator& obj);     // CopyConstructable
            iterator(iterator&& obj) noexcept; // CopyConstructable, MoveConstructable

            // Operator Overloads
            iterator& operator=(const iterator& rhs); // copy assignment operator
            iterator& operator=(iterator&& rhs);      // move assignment operator

            iterator operator++(); // incrementable
            iterator operator++(int);

            item& operator*(); // dereference operators
            item* operator->();

            bool operator==(const iterator& rhs) { return m_pos == rhs.m_pos && m_data == rhs.m_data; } // equality operator
            bool operator!=(const iterator& rhs) { return m_pos != rhs.m_pos || m_data != rhs.m_data; } // inequality operator

            size_type pos() { return m_pos; }

          private:
            size_type m_pos;
            std::vector<item>* m_data;
        };

        // Iterator methods
        iterator find(T value);
        void update(iterator i, priority_type priority);
        iterator begin();
        iterator end();
    };

    // Constructors
    template <typename T, typename R>
    priority_queue<T, R>::priority_queue() :
        m_heap({}),
        m_max_size(0),
        m_num_items(0)
    {
    }

    template <typename T, typename R>
    priority_queue<T, R>::priority_queue(std::initializer_list<std::pair<T, R>> list) :
        m_heap({}),
        m_max_size(0),
        m_num_items(0)
    {
        for (auto& [value, priority] : list)
        {
            enqueue(value, priority);
        }
    }

    // Priority queue implementation
    template <typename T, typename R>
    bool priority_queue<T, R>::isLeaf(size_type position)
    {
        return (position >= m_num_items / 2) && (position < m_num_items);
    }

    template <typename T, typename R>
    priority_queue<T, R>::size_type priority_queue<T, R>::leftChild(size_type position)
    {
        if (position >= m_num_items / 2)
        {
            return -1;
        }
        return 2 * position + 1;
    }

    template <typename T, typename R>
    priority_queue<T, R>::size_type priority_queue<T, R>::rightChild(size_type position)
    {
        if (position >= (m_num_items - 1) / 2)
        {
            return -1;
        }
        return 2 * position + 2;
    }

    template <typename T, typename R>
    priority_queue<T, R>::size_type priority_queue<T, R>::parent(size_type position)
    {
        if (position <= 0)
        {
            return -1;
        }
        return (position - 1) / 2;
    }

    template <typename T, typename R>
    void priority_queue<T, R>::enqueue(T value, R priority)
    {
        if (m_num_items >= m_max_size)
        {
            m_heap.resize(m_max_size * 1.25 + 1);
            m_max_size = m_heap.size();
        }

        size_type curr = m_num_items++;
        m_heap[curr] = item();

        m_heap[curr].value = value;
        m_heap[curr].priority = priority;

        while ((curr != 0) && (m_heap[curr].priority > m_heap[parent(curr)].priority))
        {
            std::swap(m_heap[curr], m_heap[parent(curr)]);
            curr = parent(curr);
        }
    }

    template <typename T, typename R>
    void priority_queue<T, R>::siftdown(size_type position)
    {
        if (position >= m_num_items)
        {
            return;
        }

        while (!isLeaf(position))
        {
            auto left = leftChild(position);
            if ((left < (m_num_items - 1)) && m_heap[left].priority < m_heap[left + 1].priority)
            {
                left++;
            }

            if (m_heap[position].priority >= m_heap[left].priority)
            {
                return;
            }

            std::swap(m_heap[position], m_heap[left]);
            position = left;
        }
    }

    template <typename T, typename R>
    priority_queue<T, R>::item priority_queue<T, R>::dequeue()
    {
        if (m_num_items == 0)
        {
            throw std::exception();
        }

        std::swap(m_heap[0], m_heap[--m_num_items]);
        auto dequeued = m_heap[m_num_items];
        m_heap.erase(m_heap.begin() + m_num_items);
        siftdown(0);
        return dequeued;
    }

    template <typename T, typename R>
    void priority_queue<T, R>::buildheap()
    {
        for (int i = m_num_items / 2 - 1; i >= 0; i--)
        {
            siftdown(i);
        }
    }

    // Iterator Constructors
    template <typename T, typename R>
    priority_queue<T, R>::iterator::iterator(const iterator& obj) // copy constructor
    {
        this->m_pos = obj.m_pos;
        this->m_data = obj.m_data;
    }

    template <typename T, typename R>
    priority_queue<T, R>::iterator::iterator(iterator&& obj) noexcept // move constructor
    {
        this->m_pos = obj.m_pos;
        this->m_data = obj.m_data;

        obj.m_pos = 0;
        obj.m_data = nullptr;
    }

    // Iterator Methods
    template <typename T, typename R>
    priority_queue<T, R>::iterator priority_queue<T, R>::find(T value)
    {
        for (std::uint8_t i; i < m_heap.size(); i++)
        {
            if (m_heap[i].value == value)
            {
                return iterator(i, &m_heap);
            }
        }
        return end();
    }

    template <typename T, typename R>
    void priority_queue<T, R>::update(iterator i, priority_type priority)
    {
        if (i.pos() < 0 || i.pos() >= m_num_items)
        {
            return;
        }

        i->priority = priority;

        auto curr = i.pos();

        while ((curr > 0) && (m_heap[curr].priority > m_heap[parent(curr)].priority))
        {
            std::swap(m_heap[curr], m_heap[parent(curr)]);
            curr = parent(curr);
        }
        siftdown(curr);
    }

    template <typename T, typename R>
    priority_queue<T, R>::iterator priority_queue<T, R>::begin()
    {
        if (m_num_items > 0)
        {
            return iterator(&m_heap);
        }
        return end();
    }

    template <typename T, typename R>
    priority_queue<T, R>::iterator priority_queue<T, R>::end()
    {
        return iterator(m_num_items, &m_heap);
    }

    // Misc Methods
    template <typename T, typename R>
    bool priority_queue<T, R>::empty()
    {
        return this->size() == 0;
    }

    template <typename T, typename R>
    priority_queue<T, R>::size_type priority_queue<T, R>::size()
    {
        return this->m_num_items;
    }

    // Iterator Operator Overloads
    template <typename T, typename R>
    typename priority_queue<T, R>::iterator& priority_queue<T, R>::iterator::operator=(const iterator& rhs) // copy assignment
    {
        this->m_pos = rhs.m_pos;
        this->m_data = rhs.m_data;
        return *this;
    }

    template <typename T, typename R>
    typename priority_queue<T, R>::iterator& priority_queue<T, R>::iterator::operator=(iterator&& rhs) // move assignment
    {
        if (this != &rhs)
        {
            std::swap(this->m_pos, rhs.m_pos);
            std::swap(this->m_data, rhs.m_data);
        }
        return *this;
    }

    template <typename T, typename R>
    typename priority_queue<T, R>::iterator priority_queue<T, R>::iterator::operator++() // incrementable operators
    {
        m_pos++;
        return *this;
    }

    template <typename T, typename R>
    typename priority_queue<T, R>::iterator priority_queue<T, R>::iterator::operator++(int)
    {
        iterator i = *this;
        m_pos++;
        return i;
    }

    template <typename T, typename R>
    priority_queue<T, R>::item& priority_queue<T, R>::iterator::operator*() // Dereference operators
    {
        m_pos = m_pos;
        return m_data[0][m_pos];
    }

    template <typename T, typename R>
    priority_queue<T, R>::item* priority_queue<T, R>::iterator::operator->()
    {
        return &m_data[0][m_pos];
    }
}; // namespace usu