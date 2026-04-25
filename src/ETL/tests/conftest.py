"""
Configuração do pytest para encontrar os módulos corretamente.
Este arquivo garante que os imports funcionem tanto localmente quanto no Docker.
"""
import sys
from pathlib import Path

etl_dir = Path(__file__).parent.parent
sys.path.insert(0, str(etl_dir))
