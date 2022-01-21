#include "WordTree.hpp"

#include "rlutil.h"

#include <cstdint>
#include <iostream>
#include <memory>
#include <queue>
#include <string>
#include <vector>

using TreeNodeP = std::shared_ptr<TreeNode>;

void WordTree::add(std::string word)
{
    if (word.size() == 0)
    {
        return;
    }

    WordTree::add(word, 0, m_root);
}

void WordTree::add(std::string word, std::size_t currIdx, TreeNodeP node)

{
    char currLetter = word[currIdx];

    if (!node->children[currLetter])
    {
        node->children[currLetter] = std::make_shared<TreeNode>(currIdx + 1 == word.size());
    }

    else if (currIdx == word.size() - 1)
    {
        return;
    }

    if (currIdx != word.size() - 1)
    {
        WordTree::add(word, currIdx + 1, node->children[currLetter]);
    }

    else
    {
        m_size++;
    }
}

bool WordTree::find(std::string word)
{
    if (word.size() == 0)
    {
        return false;
    }

    return WordTree::find(word, 0, m_root);
}

bool WordTree::find(std::string word, std::size_t currIdx, TreeNodeP node)
{
    char currLetter = word[currIdx];

    if (currLetter == word.back() && node->children[currLetter]->endOfWord == true)
    {
        return true;
    }

    else if (node->children[currLetter])
    {
        return WordTree::find(word, currIdx + 1, node->children[currLetter]);
    }

    else
    {
        return false;
    }
}

void search(std::uint8_t howMany, std::queue<TreeNodeP> queue, std::vector<TreeNodeP> words)
{
    if (words.size() > howMany)
    {
        return;
    }

    auto currNode = queue.front();
    queue.pop();

    if (currNode->endOfWord)
    {
        words.push_back(currNode);
    }

    for (auto& node : currNode->children)
    {
        queue.push(node.second);
    }
}

std::vector<std::string> WordTree::predict(std::string partial, std::uint8_t howMany)
{
    std::vector<std::string> words;
    TreeNodeP partialRoot = this->findPartialRoot(partial);

    if (m_root->children.empty() || partial.size() == 0 || partialRoot == m_root)
    {
        return words;
    }

    std::queue<std::pair<std::string, TreeNodeP>> queue;
    queue.push(std::make_pair(partial, partialRoot));

    while (!queue.empty() && words.size() < howMany)
    {
        auto currNode = queue.front();

        if (currNode.second->endOfWord && currNode.second != partialRoot)
        {
            words.push_back(currNode.first);
        }

        queue.pop();

        for (auto& [letterKey, node] : currNode.second->children)
        {
            queue.push(std::make_pair(currNode.first + letterKey, node));
        }
    }

    return words;
}

TreeNodeP WordTree::findPartialRoot(std::string partial)
{
    TreeNodeP node = m_root;

    for (char const& letter : partial)
    {
        if (node->children.contains(std::tolower(letter)))
        {
            node = node->children[std::tolower(letter)];
        }

        else
        {
            return m_root;
        }
    }

    return node;
}

std::size_t WordTree::size()
{
    return m_size;
}
