package kr.edcan.buspolis.util

/**
 * Created by LNTCS on 2016-10-25.
 */
class KoreanRomanizer {
    companion object{
        var capitalizeOnFirstLetter = true
        var useHyphenWhenVowelConfused = true

        @Throws(NullPointerException::class)
        fun romanize(string: String?): String {
            if (string == null) {
                throw NullPointerException()
            }

            val length = string.length
            var isFirstLetter = true
            var skipNextChosung = false
            val buffer = StringBuilder(length * 3)

            for (i in 0..length - 1) {
                var character = string[i]

                if (character.toInt() < 0xAC00 || character.toInt() > 0xD7A3) {
                    buffer.append(character)
                    isFirstLetter = true
                    continue
                }

                character -= 0xAC00

                if (!skipNextChosung) {
                    var chosung = chosungs[character.toInt() / (21 * 28)]

                    if (capitalizeOnFirstLetter && isFirstLetter && chosung.length > 0) {
                        chosung = Character.toUpperCase(chosung[0]) + chosung.substring(1)
                        isFirstLetter = false
                    }

                    buffer.append(chosung)
                }

                skipNextChosung = false

                var jungsung = jungsungs[character.toInt() % (21 * 28) / 28]

                if (capitalizeOnFirstLetter && isFirstLetter && jungsung.length > 0) {
                    jungsung = Character.toUpperCase(jungsung[0]) + jungsung.substring(1)
                    isFirstLetter = false
                }

                buffer.append(jungsung)

                var nextChosung = -1
                var nextJungsung = -1

                if (i < length - 1) {
                    val nextCharacter = string[i + 1]

                    if (nextCharacter.toInt() >= 0xAC00 && nextCharacter.toInt() <= 0xD7A3) {
                        nextChosung = (nextCharacter.toInt() - 0xAC00) / (21 * 28)
                        nextJungsung = (nextCharacter.toInt() - 0xAC00) % (21 * 28) / 28
                    }
                }

                val jongsung = character.toInt() % 28

                if (useHyphenWhenVowelConfused && jongsung == JONGSUNG_NONE && nextChosung == CHOSUNG_ㅇ) {
                    val nextJungsungChar = jungsungs[nextJungsung][0]
                    var useHyphen = false

                    when (jungsung[jungsung.length - 1]) {
                        'a' -> when (nextJungsungChar) {
                            'a', 'e' -> useHyphen = true
                        }
                        'e' -> when (nextJungsungChar) {
                            'a', 'e', 'o', 'u' -> useHyphen = true
                        }
                    }

                    if (useHyphen) {
                        buffer.append("-")
                    }
                }

                skipNextChosung = false

                when (jongsung) {
                    JONGSUNG_ㄱ -> when (nextChosung) {
                        CHOSUNG_ㄲ, CHOSUNG_ㅋ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("ng")
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("g")
                        else -> buffer.append("k")
                    }
                    JONGSUNG_ㄲ -> when (nextChosung) {
                        CHOSUNG_ㄲ, CHOSUNG_ㅋ -> {
                        }
                        CHOSUNG_ㄱ -> {
                            buffer.append("kg")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("ng")
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("kk")
                        CHOSUNG_ㅎ -> {
                            buffer.append("k")
                            skipNextChosung = true
                        }
                        else -> buffer.append("k")
                    }
                    JONGSUNG_ㄳ -> when (nextChosung) {
                        CHOSUNG_ㄲ, CHOSUNG_ㅋ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("ng")
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("ks")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> {
                            buffer.append("k")
                            skipNextChosung = true
                        }
                        else -> buffer.append("k")
                    }
                    JONGSUNG_ㄴ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("n")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                            buffer.append("g")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        else -> buffer.append("n")
                    }
                    JONGSUNG_ㄵ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("n")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                            buffer.append("g")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄷ -> {
                            buffer.append("ntt")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ, CHOSUNG_ㅎ -> {
                            skipNextChosung = true
                            buffer.append("nj")
                        }
                        CHOSUNG_ㅈ -> buffer.append("nj")
                        else -> buffer.append("n")
                    }
                    JONGSUNG_ㄶ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("n")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                            buffer.append("g")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        else -> buffer.append("n")
                    }
                    JONGSUNG_ㄷ -> when (nextChosung) {
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> when (nextJungsung) {
                            JUNGSUNG_ㅑ, JUNGSUNG_ㅒ, JUNGSUNG_ㅕ, JUNGSUNG_ㅖ, JUNGSUNG_ㅛ, JUNGSUNG_ㅠ, JUNGSUNG_ㅣ -> buffer.append("j")
                            else -> buffer.append("d")
                        }
                        CHOSUNG_ㅌ -> {
                            buffer.append("t")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        CHOSUNG_ㅎ -> {
                            buffer.append("t")
                            skipNextChosung = true
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㄹ -> when (nextChosung) {
                        CHOSUNG_ㄴ, CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> {
                            skipNextChosung = true
                            buffer.append("r")
                        }
                        CHOSUNG_ㅇ -> buffer.append("r")
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㄺ -> when (nextChosung) {
                        CHOSUNG_ㄱ, CHOSUNG_ㄲ -> {
                            buffer.append("lkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅂ -> {
                            buffer.append("lpp")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅃ, CHOSUNG_ㅊ, CHOSUNG_ㅅ, CHOSUNG_ㅋ, CHOSUNG_ㅌ, CHOSUNG_ㅍ, CHOSUNG_ㄸ -> buffer.append("l")
                        CHOSUNG_ㄷ -> {
                            buffer.append("ltt")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅁ -> buffer.append("ng")
                        CHOSUNG_ㅆ -> {
                            buffer.append("lss")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("lg")
                        CHOSUNG_ㅈ, CHOSUNG_ㅉ -> {
                            buffer.append("ljj")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> {
                            buffer.append("lk")
                            skipNextChosung = true
                        }
                        else -> buffer.append("k")
                    }
                    JONGSUNG_ㄻ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("mkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄷ -> {
                            buffer.append("mtt")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅁ -> buffer.append("l")
                        CHOSUNG_ㅂ -> {
                            buffer.append("mpp")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅅ -> {
                            buffer.append("mss")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("lm")
                        CHOSUNG_ㅈ -> {
                            buffer.append("mjj")
                            skipNextChosung = true
                        }
                        else -> buffer.append("m")
                    }
                    JONGSUNG_ㄼ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("lkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ -> buffer.append("m")
                        CHOSUNG_ㅎ -> {
                            skipNextChosung = true
                            buffer.append("p")
                        }
                        CHOSUNG_ㄷ, CHOSUNG_ㅂ, CHOSUNG_ㄸ, CHOSUNG_ㅅ, CHOSUNG_ㅆ, CHOSUNG_ㅈ, CHOSUNG_ㅉ, CHOSUNG_ㅊ, CHOSUNG_ㅋ, CHOSUNG_ㅌ -> buffer.append("p")
                        CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅃ -> {
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("lb")
                            skipNextChosung = true
                        }
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㄽ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("lkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄷ -> {
                            buffer.append("ltt")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅂ -> {
                            buffer.append("lpp")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ, CHOSUNG_ㅎ -> {
                            buffer.append("ls")
                            skipNextChosung = true
                        }
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㄾ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("lkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅂ -> {
                            buffer.append("lp")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄷ, CHOSUNG_ㅇ, CHOSUNG_ㅎ -> {
                            buffer.append("lt")
                            skipNextChosung = true
                        }
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㄿ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("lkk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ -> buffer.append("m")
                        CHOSUNG_ㄷ, CHOSUNG_ㅂ, CHOSUNG_ㄸ, CHOSUNG_ㅅ, CHOSUNG_ㅆ, CHOSUNG_ㅈ, CHOSUNG_ㅉ, CHOSUNG_ㅊ, CHOSUNG_ㅋ, CHOSUNG_ㅌ, CHOSUNG_ㅎ -> buffer.append("p")
                        CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅃ, CHOSUNG_ㅍ -> {
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("lp")
                            skipNextChosung = true
                        }
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㅀ -> when (nextChosung) {
                        CHOSUNG_ㄱ -> {
                            buffer.append("lk")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> {
                            skipNextChosung = true
                            buffer.append("r")
                        }
                        CHOSUNG_ㅇ -> buffer.append("r")
                        else -> buffer.append("l")
                    }
                    JONGSUNG_ㅁ -> when (nextChosung) {
                        CHOSUNG_ㄷ -> {
                            buffer.append("mtt")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        else -> buffer.append("m")
                    }
                    JONGSUNG_ㅂ -> when (nextChosung) {
                        CHOSUNG_ㄴ, CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅁ -> buffer.append("m")
                        CHOSUNG_ㅃ -> {
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("b")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> buffer.append("p")
                        CHOSUNG_ㅍ -> {
                            buffer.append("p")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        else -> buffer.append("p")
                    }
                    JONGSUNG_ㅄ -> when (nextChosung) {
                        CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("m")
                        CHOSUNG_ㅃ -> {
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("ps")
                            skipNextChosung = true
                        }
                        else -> buffer.append("p")
                    }
                    JONGSUNG_ㅅ -> when (nextChosung) {
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("s")
                        CHOSUNG_ㅌ, CHOSUNG_ㅎ -> {
                            buffer.append("t")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㅆ -> when (nextChosung) {
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("tss")
                        CHOSUNG_ㅌ, CHOSUNG_ㅎ -> {
                            buffer.append("t")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㅇ -> when (nextChosung) {
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("ng")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        else -> buffer.append("ng")
                    }
                    JONGSUNG_ㅈ -> when (nextChosung) {
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㅊ -> buffer.append("t")
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("j")
                        CHOSUNG_ㅌ, CHOSUNG_ㅎ -> when (nextJungsung) {
                            JUNGSUNG_ㅑ, JUNGSUNG_ㅒ, JUNGSUNG_ㅕ, JUNGSUNG_ㅖ, JUNGSUNG_ㅛ, JUNGSUNG_ㅠ, JUNGSUNG_ㅣ -> {
                                buffer.append("ch")
                                skipNextChosung = true
                            }
                            else -> {
                                buffer.append("t")
                                if (useHyphenWhenVowelConfused) {
                                    buffer.append("-")
                                }
                            }
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㅊ -> when (nextChosung) {
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> buffer.append("ch")
                        CHOSUNG_ㅌ, CHOSUNG_ㅎ -> {
                            buffer.append("t")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㅋ -> when (nextChosung) {
                        CHOSUNG_ㄲ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("ng")
                        CHOSUNG_ㄹ -> {
                            buffer.append("ngn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅎ -> {
                            buffer.append("k")
                            skipNextChosung = true
                        }
                        else -> buffer.append("k")
                    }
                    JONGSUNG_ㅌ -> when (nextChosung) {
                        CHOSUNG_ㄸ -> {
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄹ -> {
                            buffer.append("ll")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅇ -> when (nextJungsung) {
                            JUNGSUNG_ㅑ, JUNGSUNG_ㅒ, JUNGSUNG_ㅕ, JUNGSUNG_ㅖ, JUNGSUNG_ㅛ, JUNGSUNG_ㅠ, JUNGSUNG_ㅣ -> buffer.append("ch")
                            else -> buffer.append("t")
                        }
                        CHOSUNG_ㅌ -> {
                            buffer.append("t")
                            if (useHyphenWhenVowelConfused) {
                                buffer.append("-")
                            }
                        }
                        CHOSUNG_ㅎ -> {
                            buffer.append("t")
                            skipNextChosung = true
                        }
                        else -> buffer.append("t")
                    }
                    JONGSUNG_ㅍ -> when (nextChosung) {
                        CHOSUNG_ㄹ -> {
                            buffer.append("mn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅁ -> buffer.append("m")
                        CHOSUNG_ㅃ -> {
                        }
                        CHOSUNG_ㅇ -> {
                            buffer.append("p")
                            skipNextChosung = true
                        }
                        else -> buffer.append("p")
                    }
                    JONGSUNG_ㅎ -> when (nextChosung) {
                        CHOSUNG_ㅇ, CHOSUNG_ㅎ -> {
                        }
                        CHOSUNG_ㄱ -> {
                            buffer.append("k")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄷ -> {
                            buffer.append("t")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㄴ, CHOSUNG_ㅁ -> buffer.append("n")
                        CHOSUNG_ㄹ -> {
                            buffer.append("nn")
                            skipNextChosung = true
                        }
                        CHOSUNG_ㅈ -> {
                            buffer.append("ch")
                            skipNextChosung = true
                        }
                        else -> buffer.append("t")
                    }
                }
            }

            return buffer.toString()
        }
        private val chosungs = arrayOf("g", "kk", "n", "d", "tt", "r", "m", "b", "pp", "s", "ss", "", "j", "jj", "ch", "k", "t", "p", "h")
        private val jungsungs = arrayOf("a", "ae", "ya", "yae", "eo", "e", "yeo", "ye", "o", "wa", "wae", "oe", "yo", "u", "wo", "we", "wi", "yu", "eu", "ui", "i")

        val CHOSUNG_ㄱ = 0
        val CHOSUNG_ㄲ = 1
        val CHOSUNG_ㄴ = 2
        val CHOSUNG_ㄷ = 3
        val CHOSUNG_ㄸ = 4
        val CHOSUNG_ㄹ = 5
        val CHOSUNG_ㅁ = 6
        val CHOSUNG_ㅂ = 7
        val CHOSUNG_ㅃ = 8
        val CHOSUNG_ㅅ = 9
        val CHOSUNG_ㅆ = 10
        val CHOSUNG_ㅇ = 11
        val CHOSUNG_ㅈ = 12
        val CHOSUNG_ㅉ = 13
        val CHOSUNG_ㅊ = 14
        val CHOSUNG_ㅋ = 15
        val CHOSUNG_ㅌ = 16
        val CHOSUNG_ㅍ = 17
        val CHOSUNG_ㅎ = 18

        val JUNGSUNG_ㅏ = 0
        val JUNGSUNG_ㅐ = 1
        val JUNGSUNG_ㅑ = 2
        val JUNGSUNG_ㅒ = 3
        val JUNGSUNG_ㅓ = 4
        val JUNGSUNG_ㅔ = 5
        val JUNGSUNG_ㅕ = 6
        val JUNGSUNG_ㅖ = 7
        val JUNGSUNG_ㅗ = 8
        val JUNGSUNG_ㅘ = 9
        val JUNGSUNG_ㅙ = 10
        val JUNGSUNG_ㅚ = 11
        val JUNGSUNG_ㅛ = 12
        val JUNGSUNG_ㅜ = 13
        val JUNGSUNG_ㅝ = 14
        val JUNGSUNG_ㅞ = 15
        val JUNGSUNG_ㅟ = 16
        val JUNGSUNG_ㅠ = 17
        val JUNGSUNG_ㅡ = 18
        val JUNGSUNG_ㅢ = 19
        val JUNGSUNG_ㅣ = 20

        val JONGSUNG_NONE = 0
        val JONGSUNG_ㄱ = 1
        val JONGSUNG_ㄲ = 2
        val JONGSUNG_ㄳ = 3
        val JONGSUNG_ㄴ = 4
        val JONGSUNG_ㄵ = 5
        val JONGSUNG_ㄶ = 6
        val JONGSUNG_ㄷ = 7
        val JONGSUNG_ㄹ = 8
        val JONGSUNG_ㄺ = 9
        val JONGSUNG_ㄻ = 10
        val JONGSUNG_ㄼ = 11
        val JONGSUNG_ㄽ = 12
        val JONGSUNG_ㄾ = 13
        val JONGSUNG_ㄿ = 14
        val JONGSUNG_ㅀ = 15
        val JONGSUNG_ㅁ = 16
        val JONGSUNG_ㅂ = 17
        val JONGSUNG_ㅄ = 18
        val JONGSUNG_ㅅ = 19
        val JONGSUNG_ㅆ = 20
        val JONGSUNG_ㅇ = 21
        val JONGSUNG_ㅈ = 22
        val JONGSUNG_ㅊ = 23
        val JONGSUNG_ㅋ = 24
        val JONGSUNG_ㅌ = 25
        val JONGSUNG_ㅍ = 26
        val JONGSUNG_ㅎ = 27
    }
}
