# -*- coding: utf-8 -*-


def chexc(content):
    exclude = set(string.punctuation)
    content_punc_removed = ''.join(ch for ch in content if ch not in exclude)
    return content_punc_removed